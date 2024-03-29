package SFV;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Haupt.CRC;
import Haupt.Main;

/**
 * Created on 21.09.2005
 * @author 3dfx
 */

@SuppressWarnings("ForLoopReplaceableByForEach")
public class CreateSfv extends SFV implements ICreate {
	public CreateSfv() {
		super();
	}

	public int create() throws IOException {
		// SFV File stuff [START]
		if (sfv_file.equals("")) {
			File file = new File(files[0]);
			if (file.exists()) {
				if (file.isFile()) {
					sfv_path = getPathName(file.toString()) + File.separator;
					String tmp = getWorkingPath(sfv_path).replace(" ", "_");
					sfv_file = sfv_path + "_" + tmp + ".sfv";
				}
				else {
					if (files.length == 1) {
						sfv_path = file.toString() + File.separator;
						String tmp = getWorkingPath(sfv_path).replace(" ", "_");
						sfv_file = sfv_path + "_" + tmp + ".sfv";
					}
					else {
						sfv_path = file.getParent().toString() + File.separator;
						String tmp = getWorkingPath(sfv_path).replace(" ", "_");
						sfv_file = sfv_path + "_" + tmp + ".sfv";
					}
				}
			}
			else {
				System.err.print("ERROR:\t\"" + file + "\" not found!");
				return -1;
			}
		}

		File dir = new File(sfv_path);
		if (!dir.isDirectory()) {
			System.err.println("ERROR:\tUnable to get diretory of files to check...");
			return -1;
		}
		// SFV File stuff [END]

		// Date stuff [START]
		FileWriter fwriter = new FileWriter(new File(sfv_file));
		BufferedWriter out = new BufferedWriter(fwriter);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("GERMAN"));
		String datum = formatter.format(new Date());
		// Date stuff [END]

		out.write("; Generated by WIN-SFV32 v1 (" + Main.VERSION + " faking WinSFV) on " + datum + "\n");
		out.write("; Coded by 3dfx\n");
		out.write("; http://www.xfd3.de\n");
		if (!comment.equals("")) {
			out.write("; -----[ Comment ]----------------------------------------\n");
			out.write("; " + comment + "\n");
			out.write("; --------------------------------------------------------\n");
		}
		out.flush();

		for (int i = 0; i < files.length; i++) {
			calculate(files[i], out);
		}

		out.write("; keep it easy, no stress :)\n");
		out.close();
		return 0;
	}

	private void calculate(String path, BufferedWriter out) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}

		String[] rootList = new String[1];
		if (file.isDirectory()) {
			rootList = file.list();
		} else {
			rootList[0] = "";
		}

		if (rootList == null) {
			return;
		}

		List<File> dirs  = new ArrayList<>();
		List<File> files = new ArrayList<>();
		for (int i = 0; i < rootList.length; i++) {
			file = new File(path + File.separator + rootList[i]);
			if (!file.exists()) {
				continue;
			}
			if (file.isDirectory()) {
				dirs.add(file);

			} else if (file.isFile() && !isFileIgnored(file)) {
				files.add(file);
			}
		}

		rootList = null;

		for (int i = 0; i < dirs.size(); i++) {
			calculate(dirs.get(i).toString(), out);
		}

		CRC chk = new CRC(BUF_SIZE);
		for (int i = 0; i < files.size(); i++) {
			file = files.get(i);
			String res = "";
			try {
				res = chk.getCRC(file);
			} catch (IOException e) {
				res = "IO_ERROR";
			}

			if (!res.equalsIgnoreCase(Main.ER_CRC)) {
				echo("\t" + fixOutputLength(file.getName(), 50) + " " + res);
				String tmpName = "";
				if (new File(path).isDirectory()) {
					if (sfv_path != null && !sfv_path.equals(path)) {
						tmpName = path;
					}
					if (!path.endsWith(File.separator)) {
						tmpName += File.separator;
					}

					if (sfv_path != null) {
						tmpName = tmpName.replace(sfv_path, "");
					}
						tmpName += file.getName();

				} else {
					tmpName = getFileName(file.toString());
				}

				out.write(tmpName + " " + res);
				out.newLine();
				out.flush();
			}
		} // for (int i = 0; i < rootList.length; i++)
	}
}
