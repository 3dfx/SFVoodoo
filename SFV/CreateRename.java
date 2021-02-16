package SFV;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import Haupt.CRC;
import Haupt.Main;

/*
 * Created on 21.09.2005
 */
/**
 * @author 3dfx
 */

public class CreateRename extends SFV implements ICreate {
	public CreateRename() {
		super();
	}
	public int create() throws FileNotFoundException, IOException {

		for (int i = 0; i < files.length; i++)
			calculate(files[i]);

		return 0;
	}

	private void calculate(String path) throws FileNotFoundException, IOException {
		String[] nfiles = new String[1];

		File file = new File(path);
		if (file.isDirectory())
			nfiles = file.list();
		else
			nfiles[0] = file.toString();

		for (int i = 0; i < nfiles.length; i++) {
			if (file.exists()) {
				if (file.isDirectory()) {
					int j;
					boolean reached = false;
					for (j = 0; j < nfiles.length; j++) {
						nfiles[j] = path + File.separator + nfiles[j];
						file = new File(nfiles[j]);
						if (file.isDirectory())
							calculate(file.toString() + File.separator);
						else if(!reached) {
							i = j;
							reached = true;
						}
						file = null;
					} // for (j = 0; j < nfiles.length; j++)
				} // if (file.isDirectory())
			}

			file = new File(nfiles[i]);
			if (file.isFile() && !file.toString().endsWith(".sfv") && !file.toString().equalsIgnoreCase("thumbs.db")) {
				CRC chk = new CRC(nfiles[i], BUF_SIZE);
				String res = chk.getCRC();
				chk = null;
				if (!res.equalsIgnoreCase(Main.ER_CRC)) {
					echo("\t" + file.getName().toString() + " " + res);
					String tmpName = "";
					if (new File(path).isDirectory()) {
						if (sfv_path != path)
							tmpName = path;
						if (!path.endsWith(File.separator))
							tmpName += File.separator;

						tmpName = tmpName.replace(sfv_path, "");
						tmpName += file.getName().toString();
					}
					else
						tmpName = getFileName(file.toString());

					File tmp = getRenamedName(file, res);
					file.renameTo(tmp);
				}
			} // if (file.isFile()........
		} // for (int i = 0; i < nfiles.length; i++)
	}
}
