package SFV;
import Haupt.CRC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Created on 21.09.2005
 */
/**
 * @author 3dfx
 */

public class CheckSfv extends SFV implements ICheck {
	public CheckSfv() {
		super();
	}

	public CheckSfv(String filename) {
		super(filename);
	}

	public int check() throws FileNotFoundException, IOException {
		int crc_ok = 0, crc_fail = 0, crc_miss = 0, sl = 0;

		String file_name = "", checked_crc = "", tmp = "";
		double MB = 0;
		long starto = System.currentTimeMillis();

		File file = new File(sfv_file);

		if (!file.isFile()) {
			System.err.print("ERROR:\t\"" + file + "\" not found!");
			return -1;
		}
		else {
			int i = 1;
			String folder = getPathName(file.getPath().toString()) + File.separator;

			FileReader fRead = new FileReader(file);
			BufferedReader in = new BufferedReader(fRead);

			while ((tmp = in.readLine()) != null) {
				if (!tmp.equalsIgnoreCase("") && tmp.charAt(0) != ';') {
					int len = tmp.length();
					file_name = tmp.substring(0, len-9).trim();
					if (file_name.charAt(1) != ':')
						file_name = folder + file_name;

					File cur_file = new File(file_name);
					echo((i > 9 ? "" : "0") + i + ": \"" + fixOutputLength(getFileName(file_name), 70) + "\"");
					if (cur_file.exists()) {
						checked_crc = tmp.substring(len-9, len).trim();
						MB += cur_file.length();

						CRC chk = new CRC(cur_file, BUF_SIZE);
						String res = chk.getCRC();

						if (checked_crc.equalsIgnoreCase(res)) {
							echo("\tCRC OK\t\t[" + res + "]\n");
							crc_ok++;
						}
						else {
							echo("\tCRC FAIL\t[" + res + " != " + checked_crc + "]\n");
							crc_fail++;
						}
					} // is (isFile)
					else {
						echo("\t\tFnF\n");
						crc_miss++;
					}

					i++;
				} // if (line ok)
			} // while

			in.close();
		}

		long finish = System.currentTimeMillis();
		double fin = (finish - starto) / 1000.00;

		int sum = crc_ok + crc_fail + crc_miss;
		String duration = round(Double.toString(fin), 2);
		String size = round(Double.toString(MB/1024/1024), 2);
		String speed = round(Double.toString(MB/1024/1024 / fin), 2);

		if ((duration.toString() + " secs").toString().length() > 10)
			sl = 1;
		else if ((size.toString() + " MB").toString().length() > 10)
			sl = 2;
		else if ((speed.toString() + " MB/s").toString().length() > 10)
			sl = 3;

		echo("\n+=======================" + (sl != 0 ? "========" : "") + "+");
		echo("| Duration : " + duration + " secs\t" + (sl == 2 || sl == 3 ? "\t" : "") + "|");
		echo("| Size	 : " + size + " MB\t" + (sl == 1 || sl == 3 ? "\t" : "") + "|");
		echo("| Speed	: " + speed + " MB/s\t" + (sl == 1 || sl == 2 ? "\t" : "") + "|");
		echo("+-----------------------" + (sl != 0 ? "--------" : "") + "+");
		if (crc_miss > 0)
			echo("| MISS	 :\t" + crc_miss + "\t" + (sl != 0 ? "\t" : "") + "|");
		if (crc_fail > 0)
			echo("| FAIL	 :\t" + crc_fail + "\t" + (sl != 0 ? "\t" : "") + "|");
		echo("| OK	   :\t" + crc_ok + "\t" + (sl != 0 ? "\t" : "") + "|");
		echo("+-----------------------" + (sl != 0 ? "--------" : "") + "+");
		echo("| TOTAL	:\t" + sum + "\t" + (sl != 0 ? "\t" : "") + "|");
		echo("+=======================" + (sl != 0 ? "========" : "") + "+");
		return 0;
	}
}
