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
		BUF_SIZE = buffer;
	}

	public String getCRC(String filename) throws IOException {
		this.filename = filename;
		return getCRC();
	}

	public String getCRC(File file) throws IOException {
		this.cfile = file;
		return getCRC();
	}

	public String getCRC() throws IOException {
		File file = (cfile == null ? new File(filename) : cfile);
		if (!file.exists() || !file.isFile()) {
			return Main.ER_CRC;
		}

		CRC_32.reset();
		read_buffer = new byte[BUF_SIZE];
		inputStream = new BufferedInputStream(new FileInputStream(file));

		double bytesLeft = file.length();
		double size = bytesLeft;
		int proz = 100;
		double p = 0;

		System.out.print("[");
		while (inputStream.read(read_buffer, 0, BUF_SIZE) != -1) {
			if (bytesLeft > read_buffer.length) {
				CRC_32.update(read_buffer);
			} else {
				CRC_32.update(read_buffer, 0, (int) bytesLeft);
			}

			bytesLeft -= read_buffer.length;
			p = (double)(bytesLeft / size * 100.00);
			if (p < proz && proz > 0) {
				System.out.print("|");
				proz -= 10;
			}
		}
		inputStream.close();

		while (proz > 0) {
			System.out.print("|");
			proz -= 10;
		}
		System.out.print("]");

		CRC_VAL.setLength(0);
		CRC_VAL.insert(0, Long.toHexString(CRC_32.getValue()).toUpperCase());
		while (CRC_VAL.length() < 8) {
			CRC_VAL.insert(0, "0");
		}

		return CRC_VAL.toString();
	}
}