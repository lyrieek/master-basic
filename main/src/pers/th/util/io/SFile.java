package pers.th.util.io;

import java.io.File;

/**
 * 在内存较少的情况下操作大量文件
 * @author 天浩
 *
 */
public class SFile {
	
	char[] path;
	
	public SFile(String file) {
		path = file.toCharArray();
	}
	
	public SFile(char[] fileName) {
		path = fileName;
	}
	
	public boolean delete() {
		return getFile().delete();
	}
	
	public File getFile() {
		return new File(new String(path));
	}

}
