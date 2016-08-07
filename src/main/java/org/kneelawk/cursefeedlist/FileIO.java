package org.kneelawk.cursefeedlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class FileIO {
	public static void writeToFile(File out, String data) throws FileNotFoundException, IOException {
		try (PrintStream ps = new PrintStream(out)) {
			ps.print(data);
		}
	}

	public static void writeToFile(File out, InputStream is) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(out)) {
			byte[] buf = new byte[8192];
			int read;
			while ((read = is.read(buf)) >= 0) {
				fos.write(buf, 0, read);
			}
		}
	}

	public static void writeToFile(File out, byte[] buf) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(out)) {
			fos.write(buf);
		}
	}

	public static String readStringFromFile(File in) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(in)) {
			String content = "";

			byte[] buf = new byte[256];
			int read;
			while ((read = fis.read(buf)) >= 0) {
				content += new String(buf, 0, read);
			}

			return content;
		}
	}

	public static byte[] readBytesFromFile(File in) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(in)) {
			byte[] data = new byte[fis.available()];
			fis.read(data);
			return data;
		}
	}
}
