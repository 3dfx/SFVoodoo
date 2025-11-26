package SFV;

import Haupt.CRC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created on 21.09.2005
 * @author 3dfx
 */

public class CheckSfv extends SFV implements ICheck {

	public CheckSfv() {
		super();
	}

	public CheckSfv(String filename) {
		super(filename);
	}

	public int check() throws IOException {
		String file_name = "", checked_crc = "", tmp = "";
		long starto = System.currentTimeMillis();

		File file = new File(sfv_file);
		if (!file.exists() || !file.isFile()) {
			System.err.print("ERROR:\t\"" + file + "\" not found!");
			return -1;

		} else {
			int i = 1;
			String folder = getPathName(file.getPath()) + File.separator;

			FileReader fRead = new FileReader(file);
			BufferedReader in = new BufferedReader(fRead);

			CRC chk = new CRC(BUF_SIZE);
			while ((tmp = in.readLine()) != null) {
				if (!tmp.equalsIgnoreCase("") && tmp.charAt(0) != ';') {
					int len = tmp.length();
					file_name = tmp.substring(0, len - 9).trim();
					if (file_name.charAt(1) != ':') {
						file_name = folder + file_name;
					}

					File cur_file = new File(file_name);
					//echo((i > 9 ? "" : "0") + i + ": \"" + fixOutputLength(CRC.getFileName(file_name), 70) + "\"");
					echo((i > 9 ? "" : "0") + i + ": \"" + folder + CRC.getFileName(file_name) + "\"");
					if (cur_file.exists()) {
						checked_crc = tmp.substring(len - 9, len).trim();
						MB += cur_file.length();

						String res = "";
						try {
							res = chk.getCRC(cur_file);
						} catch (IOException e) {
							res = "IO_ERROR";
						}

						if (checked_crc.equalsIgnoreCase(res)) {
							echo("\tCRC OK\t\t[" + res + "]\n");
							crc_ok++;
						} else {
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

		printResults((System.currentTimeMillis() - starto) / 1000.00);
		return 0;
	}
}
