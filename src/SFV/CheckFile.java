package SFV;

import Haupt.CRC;

import java.io.File;
import java.io.IOException;

/**
 * Created on 21.09.2005
 * @author 3dfx
 */

@SuppressWarnings("ForLoopReplaceableByForEach")
public class CheckFile extends SFV implements ICheck {
	public CheckFile() {
		super();
	}

	public int check() throws IOException {
		long starto = System.currentTimeMillis();

		for (int i = 0; i < files.length; i++) {
			check_that(files[i]);
		}

		long finish = System.currentTimeMillis();
		double fin = (finish - starto) / 1000.00;

		printResults(fin);
		return 0;
	}

	private void check_that(String path) {
		String[] nfiles = new String[1];

		File file = new File(path);
		if (file.isDirectory()) {
			nfiles = file.list();
		} else {
			nfiles[0] = file.toString();
		}

		if (nfiles == null) {
			return;
		}

		CRC chk = new CRC(BUF_SIZE);
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
						check_that(file + File.separator);
					else if(!reached) {
						i = j;
						reached = true;
					}
					file = null;
				} // for (j = 0; j < nfiles.length; j++)
			} // if (file.isDirectory())

			file = new File(nfiles[i]);
			if (file.isFile() && !isFileIgnored(file)) {
				String fName = CRC.getFileName(nfiles[i]);
                String checked_crc = CRC.getCRCFromFileName(fName); // CRC des zu checkenden files holen

				echo((count > 9 ? "" : "0") + count + ": \"" + fName + "\"");
				if (!checked_crc.isEmpty()) {
					int pos = 0;
					MB += file.length();

					String res = "";
					try {
						res = chk.getCRC(nfiles[i]);
					} catch (IOException e) {
						res = "IO_ERROR";
					}

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
