package SFV;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import Haupt.CRC;

/*
 * Created on 21.09.2005
 */
/**
 * @author 3dfx
 */

public class CheckFile extends SFV implements ICheck {
	public CheckFile() {
		super();
	}
	int crc_ok = 0, crc_fail = 0, crc_miss = 0, no_crc = 0, sl = 0, count = 1;;
	String file_name = "", checked_crc = "", tmp = "";
	double MB = 0;

	public int check() throws FileNotFoundException, IOException {
		long starto = System.currentTimeMillis();

		for (int i = 0; i < files.length; i++) {
			check_that(files[i]);
		}

		long finish = System.currentTimeMillis();
		double fin = (finish - starto) / 1000.00;

		int sum = crc_ok + crc_fail + no_crc;
		String duration = round(Double.toString(fin), 2);
		String size = round(Double.toString(MB/1024/1024), 2);
		String speed = round(Double.toString(MB/1024/1024 / fin), 2);

		if ((duration.toString() + " secs").toString().length() > 10)
			sl = 1;
		else if ((size.toString() + " MB").toString().length() > 10)
			sl = 2;
		else if ((speed.toString() + " MB/s").toString().length() > 10)
			sl = 3;

		//sl = 1;

		echo("\n+=======================" + (sl != 0 ? "========" : "") + "+");
		echo("| Duration : " + duration + " secs\t" + (sl == 2 || sl == 3 ? "\t" : "") + "|");
		echo("| Size	 : " + size + " MB\t" + (sl == 1 || sl == 3 ? "\t" : "") + "|");
		echo("| Speed	: " + speed + " MB/s\t" + (sl == 1 || sl == 2 ? "\t" : "") + "|");
		echo("+-----------------------" + (sl != 0 ? "--------" : "") + "+");
		if (no_crc > 0)
			echo("| !CRC	 :\t" + no_crc + "\t" + (sl != 0 ? "\t" : "") + "|");
		if (crc_fail > 0)
			echo("| FAIL	 :\t" + crc_fail + "\t" + (sl != 0 ? "\t" : "") + "|");
		echo("| OK	   :\t" + crc_ok + "\t" + (sl != 0 ? "\t" : "") + "|");
		echo("+-----------------------" + (sl != 0 ? "--------" : "") + "+");
		echo("| TOTAL	:\t" + sum + "\t" + (sl != 0 ? "\t" : "") + "|");
		echo("+=======================" + (sl != 0 ? "========" : "") + "+");
		return 0;
	}

	private void check_that(String path) throws FileNotFoundException, IOException {
		String[] nfiles = new String[1];

		File file = new File(path);
		if (file.isDirectory())
			nfiles = file.list();
		else
			nfiles[0] = file.toString();

		for (int i = 0; i < nfiles.length; i++) {
			if (!file.exists()) {
				break;
			}

			if (file.isDirectory()) {
				int j;
				boolean reached = false;
				for (j = 0; j < nfiles.length; j++) {
					nfiles[j] = path + File.separator + nfiles[j];
					file = new File(nfiles[j]);
					if (file.isDirectory())
						check_that(file.toString() + File.separator);
					else if(!reached) {
						i = j;
						reached = true;
					}
					file = null;
				} // for (j = 0; j < nfiles.length; j++)
			} // if (file.isDirectory())

			file = new File(nfiles[i]);
			if (file.isFile() && !file.toString().endsWith(".sfv") && !file.toString().equalsIgnoreCase("thumbs.db")) {
				char mch_op = 0, mch_ed = 0;
				if (nfiles[i].matches(".*\\[[A-Fa-f0-9]{8}\\].*")) {
					mch_op = '[';	mch_ed = ']';
				}
				else if (nfiles[i].matches(".*\\([A-Fa-f0-9]{8}\\).*")) {
					mch_op = '(';	mch_ed = ')';
				}
				else if (nfiles[i].matches(".*\\_[A-Fa-f0-9]{8}\\_.*")) {
					mch_op = '_';	mch_ed = '_';
				}

				String fName = getFileName(nfiles[i]);
				echo((count > 9 ? "" : "0") + count + ": \"" + fName + "\"");
				if (mch_op == '[' || mch_op == '(' || mch_op == '_') {
					int pos = 0;
					MB += file.length();

					// CRC des zu checkenden files holen
					while ((fName.charAt(pos) != mch_op) || (fName.charAt(pos+9) != mch_ed))
						pos++;

					checked_crc = fName.substring(pos+1, pos+9);
					// CRC des zu checkenden files holen

					CRC chk = new CRC(nfiles[i], BUF_SIZE);
					String res = chk.getCRC(); // CRC errechnen

					if (checked_crc.equalsIgnoreCase(res)) {
						echo("\tCRC OK\t\t[" + res + "]\n");
						crc_ok++;
					}
					else {
						echo("\tCRC FAIL\t[" + res + " != " + checked_crc + "]\n");
						crc_fail++;
					}
				}
				else {
					echo("\t\tNO CRC\n");
					no_crc++;
				}

				count++;
			} // if (file.isFile()........
		} // for (int i = 0; i < nfiles.length; i++)
	}
}
