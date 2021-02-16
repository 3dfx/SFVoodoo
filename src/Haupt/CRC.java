package Haupt;
import java.io.*;
import java.util.zip.CRC32;

/*
 * Created on 09.12.2004
 *
 */
/**
 * @author 3dfx
 */

public class CRC {
	int BUF_SIZE;
	String filename;
	File cfile;
	String crc_val;

	public CRC(File filename, int buffer) {
		this.cfile = filename;
		BUF_SIZE = buffer;
		crc_val = Main.ER_CRC;
	}

	public CRC(String filename, int buffer) {
		this.filename = filename;
		BUF_SIZE = buffer;
		crc_val = Main.ER_CRC;
	}

	public String getCRC() throws IOException {
		File file = (cfile == null ? new File(filename) : cfile);

		if (!file.isFile()) {
			return Main.ER_CRC;
		}

		CRC32 crc = new CRC32();
		byte[] buf = new byte[BUF_SIZE];
		FileInputStream src = new FileInputStream(file);
		BufferedInputStream input = new BufferedInputStream(src);

		double bytesLeft = file.length();
		double size = bytesLeft;
		int proz = 100;
		double p = 0;
		System.out.print("[");
		while (input.read(buf, 0, BUF_SIZE) != -1) {
			if (bytesLeft > buf.length) {
				crc.update(buf);
			} else {
				crc.update(buf, 0, (int) bytesLeft);
			}

			bytesLeft -= buf.length;
			p = (double)(bytesLeft / size * 100.00);
			if (p < proz && proz > 0) {
				System.out.print("|");
				proz -= 10;
			}
		}
		while (proz > 0) {
			System.out.print("|");
			proz -= 10;
		}
		System.out.print("]");

		crc_val = Long.toHexString(crc.getValue()).toUpperCase();
		while (crc_val.length() < 8) {
			crc_val = "0" + crc_val;
		}

		input.close();
		return crc_val;
	}
}