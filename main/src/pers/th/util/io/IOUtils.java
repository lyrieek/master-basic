package pers.th.util.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IOUtils {

	/**
	 * 直接读取文件
	 * 
	 * @param path
	 * @return
	 */
	public static String reader(String path) {
		File file = XFile.getFile(path);
		if (file == null || !file.isFile()) {
			return "";
		}
		int length = 0;
		byte[] buffer = new byte[512];
		StringBuffer template = new StringBuffer();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			while ((length = fis.read(buffer)) != -1) {
				template.append(new String(buffer, 0, length));
			}
			return template.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(fis);
		}
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				throw new RuntimeException("can't close:" + e);
			}
		}
	}

}
