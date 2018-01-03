package pers.th.util.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XFile extends File {

	private static final long serialVersionUID = 1L;

	public XFile(String pathname) {
		super(pathname);
	}

	public static void main(String[] args) {
		new XFile("D:\\Mozilla Firefox").printf();
	}

	public void write(String context) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(this);
			fos.write(context.getBytes());
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(fos);
		}
	}

	public boolean move(File file) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (!file.createNewFile()) {
				return false;
			}
			fis = new FileInputStream(this);
			fos = new FileOutputStream(file);
			int length = -1;
			byte[] buffer = new byte[8142];
			while ((length = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, length);
				fos.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(fis, fos);
		}
		return true;
	}

	public void printf() {
		File[] files = listFiles();
		for (File iterable_element : files) {
			System.out.println(iterable_element);
		}
		System.out.println("Totel: " + files.length);
	}

	public boolean rm() {
		return rm(this);
	}

	public static void close(Closeable... cArr) {
		if (cArr == null) {
			return;
		}
		try {
			for (Closeable closeable : cArr) {
				if (closeable != null) {
					closeable.close();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("can't close", e);
		}
	}

	public static boolean rm(File file) {
		if (file.isFile()) {
			return file.delete();
		}
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				rm(item);
			}
			file.delete();
		}
		return !file.exists();
	}

}
