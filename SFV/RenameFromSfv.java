package SFV;
import Haupt.CRC;
import Haupt.Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * Created on 21.09.2005
 */
/**
 * @author 3dfx
 */

public class RenameFromSfv extends SFV implements ICheck {
	public RenameFromSfv() {
		super();
	}

	public RenameFromSfv(String filename) {
		super(filename);
	}

	public int check() throws FileNotFoundException, IOException {
		int crc_ok = 0, crc_fail = 0, crc_miss = 0, sl = 0;

		String file_name = "", file_crc = "", checked_crc = "", tmp = "";
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

			List sfvList = new ArrayList();
			List crcList = new ArrayList();

			FileWriter fwriter = new FileWriter(new File(folder + "__undo.bat"));
			BufferedWriter out = new BufferedWriter(fwriter);

			while ((tmp = in.readLine()) != null) {
				if (!tmp.equalsIgnoreCase("") && tmp.charAt(0) != ';') {
					int len = tmp.length();
					file_name = tmp.substring(0, len-9).trim();
					file_crc = tmp.substring(len-8, len).trim();

					sfvList.add(file_name);
					crcList.add(file_crc);

					i++;
				} // if (line ok)
			} // while

			in.close();

			File path = new File(folder);
			String[] nfiles = null;
			if (path.isDirectory())
				nfiles = path.list();

			int rmd = 0;
			for (i = 0; i < nfiles.length; i++) {
				char mch_op = 0, mch_ed = 0;

				tmp = nfiles[i];
				if (tmp.matches(".*\\[[A-Fa-f0-9]{8}\\].*")) { mch_op = '[';   mch_ed = ']'; }
				else if (tmp.matches(".*\\([A-Fa-f0-9]{8}\\).*")) { mch_op = '(';   mch_ed = ')'; }
				else if (tmp.matches(".*\\_[A-Fa-f0-9]{8}\\_.*")) { mch_op = '_';   mch_ed = '_'; }

				if (mch_op != 0) {
					tmp = tmp.substring(tmp.indexOf(mch_op)+1, tmp.indexOf(mch_ed));
					if (crcList.contains(tmp)) {
						int pos = crcList.indexOf(tmp);
						file = new File(folder + nfiles[i]);
						if (file.isFile()) {
							file.renameTo(new File(folder + sfvList.get(pos)));
							echo("renaming: \"" + nfiles[i] + "\" => \"" + sfvList.get(pos) + "\"");
							if (rmd == 0)
								out.write("@echo off\ncls\necho SFVoodoo undo script...\n\n");

							out.write("echo renaming \"" + sfvList.get(pos) + "\" to \"" + nfiles[i] + "\"\n");
							out.write("rename \"" + sfvList.get(pos) + "\" \"" + nfiles[i] + "\"\n\n");
							rmd++;
						}
					}
				}
			}

			out.write("pause\n");
			out.flush();
			out.close();
		}

		return 0;
	}

	private int getLines(String fileName) throws IOException {
		FileReader fRead = new FileReader(fileName);
		BufferedReader in = new BufferedReader(fRead);

		int lines = 0;
		String tmp;
		while ((tmp = in.readLine()) != null) {
			if (!tmp.equalsIgnoreCase("") && tmp.charAt(0) != ';') {
				lines++;
			}
		}

		return lines;
	}
}
