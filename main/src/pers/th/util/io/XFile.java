package pers.th.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pers.th.util.Arrays;

/**
 * 文件处理
 * @author 天浩
 *
 */
public class XFile extends File {

	private static final long serialVersionUID = 1L;

	public XFile(String pathname) {
		super(pathname);
	}

	public static void main(String[] args) {
		new XFile("D:\\Mozilla Firefox").printf();
	}

	public void writeAndClose(String context) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(this);
			fos.write(context.getBytes());
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Arrays.close(fos);
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
			Arrays.close(fis, fos);
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

	/**
	 * 递归删除一个文件/目录
	 * @param file
	 * @return
	 */
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
	
	/**
	 * 获取一个文件
	 * @param path
	 * @return
	 */
	public static File getFile(String path) {
		if (path == null || path.trim().isEmpty()) {
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		return file;
	}
	
	/**
	 * 直接读取文件
	 * @param path
	 * @return
	 */
	public static String reader(String path) {
		File file = getFile(path);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return template.toString();
	}

	/**
	 * 获取目录下的文件并递归子目录的文件
	 * @param path
	 * @return
	 */
	public static List<File> listFile(String path) {
		List<File> files = new ArrayList<>();
		File file = getFile(path);
		if (file == null || !file.isDirectory()) {
			return null;
		}
		for (File items : file.listFiles()) {
			if (items.isDirectory()) {
				files.addAll(listFile(items.getAbsolutePath()));
			} else {
				files.add(items);
			}
		}
		return files;
	}
	
	public void each(FileHandle handle) {
		if (isFile()) {
			if (handle.filter(this)) {
				handle.run(this);
			}
			return;
		}
		each(listFiles(), handle);
	}

	public static void each(File[] listFile, FileHandle handle) {
		for (File file : listFile) {
			if (handle.filter(file)) {
				handle.run(file);
			}
		}
	}
}
