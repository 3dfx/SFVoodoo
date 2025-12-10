package Haupt;

import java.io.*;
import java.util.zip.CRC32;

/**
 * Created on 09.12.2004
 * @author 3dfx
 */

public class CRC {
	public static final int BUF_SIZE_DEFAULT = Main.BUF_SIZE_DEFAULT;
	protected int BUF_SIZE = BUF_SIZE_DEFAULT;
//	String crc_val = Main.ER_CRC;

	private static final StringBuilder CRC_VAL = new StringBuilder(8);
	private static final CRC32 CRC_32 = new CRC32();

	private static BufferedInputStream inputStream = null;
	private static byte[] read_buffer = null;

	String filename;
	File cfile;

	public CRC() {
	}

	public CRC(int buffer) {
		this.BUF_SIZE = buffer;
	}

	public CRC(File file) {
		this.cfile = file;
	}

	public CRC(File filename, int buffer) {
		this.cfile = filename;
		BUF_SIZE = buffer;
	}

	public CRC(String filename) {
		this.filename = filename;
	}

	public CRC(String filename, int buffer) {
		this.filename = filename;
        this.cfile =  new File(filename);
		BUF_SIZE = buffer;
	}

	public String getCRC(String filename) throws IOException {
		this.filename = filename;
        this.cfile =  new File(filename);
		return this.getCRC();
	}

	public String getCRC(File file) throws IOException {
		this.cfile = file;
		return this.getCRC();
	}

	public String getCRC() throws IOException {
		if (!this.cfile.exists() || !this.cfile.isFile()) {
			return Main.ER_CRC;
		}

		CRC_32.reset();
		read_buffer = new byte[BUF_SIZE];
		inputStream = new BufferedInputStream(new FileInputStream(this.cfile));

		long size = this.cfile.length();
        long bytesLeft = size;
        long bytesRead = 0;
		int proz = 0;
		long p = 0;

		System.out.print("[");
		while (inputStream.read(read_buffer, 0, BUF_SIZE) != -1) {
			if (bytesLeft > read_buffer.length) {
				CRC_32.update(read_buffer);
			} else {
				CRC_32.update(read_buffer, 0, (int) bytesLeft);
			}

			bytesLeft -= read_buffer.length;
            bytesRead += read_buffer.length;
			p = (long) (((double) bytesRead / size * 100));
			if (p == proz && proz < 100) {
				System.out.print("|");
				proz += 10;
			}
		}
		inputStream.close();

		while (proz < 100) {
			System.out.print("|");
			proz += 10;
		}
		System.out.print("]");

		CRC_VAL.setLength(0);
		CRC_VAL.insert(0, Long.toHexString(CRC_32.getValue()).toUpperCase());
		while (CRC_VAL.length() < 8) {
			CRC_VAL.insert(0, "0");
		}

		return CRC_VAL.toString();
	}

    public static String getCRCFromFileName(String fName) {
        String found_crc = "";

        char mch_op = 0, mch_ed = 0;
        if (fName.matches(".*\\[[A-Fa-f0-9]{8}\\].*")) {
            mch_op = '[';	mch_ed = ']';
        }
        else if (fName.matches(".*\\([A-Fa-f0-9]{8}\\).*")) {
            mch_op = '(';	mch_ed = ')';
        }
        else if (fName.matches(".*\\_[A-Fa-f0-9]{8}\\_.*")) {
            mch_op = '_';	mch_ed = '_';
        }

        if (mch_op == '[' || mch_op == '(' || mch_op == '_') {
            int pos = 0;
            // CRC des zu checkenden files holen
            while ((fName.charAt(pos) != mch_op) || (fName.charAt(pos+9) != mch_ed)) {
                pos++;
            }

            found_crc = fName.substring(pos+1, pos+9);
        }

        return found_crc;
    }

    public static String getFileName(String file) {
        int i = file.length()-1;
        while (i > 0 && !String.valueOf(file.charAt(i)).equals(File.separator)) {
            i--;
        }

        return file.substring(i+1, file.length());
    }
}