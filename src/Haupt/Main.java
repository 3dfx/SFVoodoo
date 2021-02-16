package Haupt;
import java.io.*;
import SFV.*;

/*
 * Created on 09.12.2004
 */
/**
 * @author 3dfx
 */

public final class Main {
	private static void echo(String str) { System.out.println(str); }
	public static final String VERSION = "SFVoodoo v1.2.0";
	public static final String TITLE = "+========================+\n|	" + VERSION + "	|\n+========================+\n";
	public static final String ER_CRC = "00000000";
	public static final int BUFFER = 1024;

	public static void main (String[] args) {
		Main app = new Main();
		app.run(args);
	}

	public void run (String[] args) {
		int res = 0;
		echo(TITLE);

		if (args.length == 0) {
			echo("No arguements given.....");
			echo("Correct arguements are:\n");
			echo("===================================================");
			echo("Creating an sfv file:");
			echo("\tcreate foo.sfv file1.xxx file2.yyy file3.zzz");
			echo("Or:");
			echo("\tcreate file1.xxx file2.yyy file3.zzz");
			echo("Or:");
			echo("\tfolders folder1 folder2 folder3");
			echo("===================================================");
			echo("Checking files with CRC in filename:");
			echo("\t\"file1 [1337BABE].xxx\" \"file2 (1337BABE).yyy\" \"file3 _1337BABE_.zzz\"");
			echo("===================================================");
			echo("To append the CRCs to the filename:");
			echo("\trename file1.xxx file2.yyy file3.zzz");
			echo("===================================================");
			echo("Checking an sfv file:");
			echo("\tfoo.sfv");
			echo("===================================================");
			echo("Renaming files from an sfv file:");
			echo("\tsfvin foo.sfv");
			res = -1;
		}
		else {
			if (args[0].equalsIgnoreCase("create") ||
				args[0].equalsIgnoreCase("folders") ||
				args[0].equalsIgnoreCase("rename")) {

				ICreate oSfv = SfvObjFab.get(args[0]);
				if (oSfv == null) {
					System.exit(-1);
				}

				if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("folders")) {
					System.out.print("Enter Comment: ");
					((SFV) oSfv).set_comment(TextIO.getln());
				} else {
					System.out.print("Press Enter to start!");
					TextIO.getln();
				}
				System.out.println();

				if (args.length > 1) {
					String[] tmp = new String[args.length-1];
					System.arraycopy(args, 1, tmp, 0, args.length-1);

					((SFV) oSfv).set_files(tmp);
				}

				try {
					res = oSfv.create();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // create, rename, folders
			else {
				ICheck oSfv = null;

				if (args[0].equalsIgnoreCase("sfvin")) {
					oSfv = new RenameFromSfv(args[1]);
					((SFV) oSfv).set_bufsize(BUFFER);
				} // sfvin
				else {
					boolean mode = args[0].endsWith(".sfv");

					if (mode) {
						oSfv = new CheckSfv(args[0]);
					} else {
						oSfv = new CheckFile();
					}

					((SFV) oSfv).set_bufsize(BUFFER);

					if (!mode) {
						String[] tmp = new String[args.length];
						System.arraycopy(args, 0, tmp, 0, args.length);

						((SFV) oSfv).set_files(tmp);
					}
				} // check

				try {
					res = oSfv.check();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (res != -1) {
					int ch = '0';
					while (ch == '0') {
						try {
							ch = System.in.read();
						} catch (IOException e) {
							System.exit(0);
						}
					}
				}
			}
		}

		System.exit(res);
	}
}
