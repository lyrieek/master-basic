package pers.th.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import pers.th.util.Arrays;

/**
 * 文件处理
 * 
 * @author 天浩
 *
 */
public class XFile extends File {

	private static final long serialVersionUID = 1L;
	private Charset charset = Charset.defaultCharset();


	public XFile(String pathname) {
		super(pathname);
	}


	public XFile(String pathname, Charset charset) {
		super(pathname);
		this.charset = charset;
	}

	public static void main(String[] args) {
		new XFile("D:\\Mozilla Firefox").printf();
	}

	/**
	 * 立刻开启一个追加流输出文本并关闭流
	 * 
	 * @param context
	 */
	public void writeAndClose(String context) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(this, true);
			fos.write(context.getBytes(charset));
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Arrays.close(fos);
		}
	}

	@Override
	public File[] listFiles() {
		String[] list = list();
		if (list == null) return new File[]{};
		File[] fs = new File[list.length];
		for (int i = 0; i < fs.length; i++) {
			fs[i] = new File(list[i]);
		}
		return fs;
	}

	/**
	 * 清空文件内容
	 */
	public void clear() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(this);
			fos.write(new byte[0]);
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Arrays.close(fos);
		}
	}

	/**
	 * 复制文件到file
	 * 
	 * @param file
	 * @return
	 */
	public boolean copyTo(File file) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (!file.createNewFile()) {
				return false;
			}
			fis = new FileInputStream(this);
			fos = new FileOutputStream(file);
			int length;
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
	 * 
	 * @param file
	 * @return
	 */
	public static boolean rm(File file) {
		if (file.isFile()) {
			return file.delete();
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File item : files) {
				    rm(item);
				}
			}
			return file.delete();
		}
		return !file.exists();
	}

	/**
	 * 获取一个文件
	 * 
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
	
	public String context() {
		return IOUtils.reader(getPath());
	}
	

	/**
	 * 获取目录下的文件并递归子目录的文件
	 * 
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
