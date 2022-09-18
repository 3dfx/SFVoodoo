package SFV;
import java.io.*;

/**
 * Created on 21.09.2005
 * @author 3dfx
 */

public class CreateSfvFolders extends SFV implements ICreate {
	public CreateSfvFolders() {
		super();
	}

	public int create() throws IOException {
		ICreate oSfv = null;
		String[] tmp = null;
		int res = 0;

		for (int i = 0; i < files.length; i++) {
			tmp = new String[1];
			System.arraycopy(files, i, tmp, 0, 1);

			oSfv = new CreateSfv();
			((SFV) oSfv).set_files(tmp);
			((SFV) oSfv).set_comment(comment);
			res += oSfv.create();
		}

		return res;
	}
}
