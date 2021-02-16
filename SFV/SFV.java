package SFV;
import java.io.*;

//import sun.security.provider.MD5;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;

/*
 * Created on 09.12.2004
 */
/**
 * @author 3dfx
 */

public class SFV {
	protected static void echo(String str) { System.out.println(str); }

	String sfv_file;
	String sfv_path;
	String[] files;
	String mode;
	String comment;

	int BUF_SIZE = 1024;

	public SFV () {
		mode = "";
		comment = "";
		this.sfv_file = "";
		this.sfv_path = "";
	}

	public SFV (String filename) {
		mode = "";
		comment = "";
		this.sfv_file = filename;
		this.sfv_path = getPathName(filename);
	}

	public void set_files(String[] files) { this.files = files; }
	public void set_mode(String mode) { this.mode = mode; }
	public void set_sfv(String sfv) { this.sfv_file = sfv; }
	public void set_comment(String comment) { this.comment = comment; }
	public void set_bufsize(int buf) { this.BUF_SIZE = buf; }

	protected File getRenamedName(File file, String CRC) {
		String fName = file.toString();
		String fType = getFileType(fName);

		String fFile = fName.substring(0, fName.length() - (fType.length() + 1));
		return new File (fFile + " [" + CRC + "]" + "." + fType);
	}

	protected String getFileType(String file) {
		int i = file.length()-1;
		while (!String.valueOf(file.charAt(i)).equals("."))
			i--;

		return file.substring(i+1, file.length());
	}

	protected String fixOutputLength(String file, int max) {
		if (file.length() > max) {
			String tmp = file.substring(0, max-3);
			while (tmp.charAt(tmp.length()-1) == ' ')
				tmp = tmp.substring(0, tmp.length()-1);

			return tmp + "...";
		}
		else
			return file;
	}

	protected String getFileName(String file) {
		int i = file.length()-1;
		while (!String.valueOf(file.charAt(i)).equals(File.separator))
			i--;

		return file.substring(i+1, file.length());
	}

	protected String getPathName(String path) {
		int i = path.length()-1;
		while (!String.valueOf(path.charAt(i)).equals(File.separator))
			i--;

		return path.substring(0, i);
	}

	protected String getWorkingPath(String path) {
		if (path.length() == 3)
			return String.valueOf(path.charAt(0));

		int i = path.length()-2;
		while (!String.valueOf(path.charAt(i)).equals(File.separator))
			i--;

		return path.substring(i+1, path.length()-1);
	}

	protected String round(String number, int len) {
		len += 1;
		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) == '.') {
				if (len > 1)
					len += i;
				if (len > number.length())
					len = number.length()-1;
				if (number.charAt(len-1) == '.')
					len -= 1;

				number = number.substring(0, i) + number.substring(i, len);
				return number;
			}
		}

		return number;
	}
}