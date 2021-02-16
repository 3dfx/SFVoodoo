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
	public static final String VERSION = "SFVoodoo v1.1.1b";
	public static final String TITLE = "+========================+\n|	" + VERSION + "	|\n+========================+\n";
	public static final String ER_CRC = "00000000";
	public static final int BUFFER = 1024;

	public static void main (String[] args) {
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

				ICreate oSfv = null;
				oSfv = SfvObjFab.get(args[0]);

				if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("folders")) {
					System.out.print("Enter Comment: ");
					((SFV) oSfv).set_comment(TextIO.getln());
					System.out.println();
				}
				else {
					System.out.print("Press Enter to start!");
					TextIO.getln();
					System.out.println();
				}

				if (args.length > 1) {
					String[] tmp = new String[args.length-1];
					System.arraycopy(args, 1, tmp, 0, args.length-1);

					((SFV) oSfv).set_files(tmp);
				}

				try {
					res = ((ICreate) oSfv).create();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // create, rename, folders
			else if (args[0].equalsIgnoreCase("sfvin")) {
				ICheck oSfv;

				oSfv = new RenameFromSfv(args[1]);
				((SFV) oSfv).set_bufsize(BUFFER);

				try {
					res = oSfv.check();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				int ch = '0';
				while (ch == '0') {
					try { ch = System.in.read(); }
					catch (IOException e) {}
				}
			} // sfvin
			else {
				boolean mode = args[0].endsWith(".sfv");
				ICheck oSfv;

				if (mode)
					oSfv = new CheckSfv(args[0]);
				else
					oSfv = new CheckFile();

				((SFV) oSfv).set_bufsize(BUFFER);

				if (!mode) {
					String[] tmp = new String[args.length];
					System.arraycopy(args, 0, tmp, 0, args.length);

					((SFV) oSfv).set_files(tmp);
				}

				try {
					res = oSfv.check();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				int ch = '0';
				while (ch == '0') {
					try { ch = System.in.read(); }
					catch (IOException e) {}
				}
			} // check
		}

		System.exit(res);
	}
}
