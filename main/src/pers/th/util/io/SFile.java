package pers.th.util.io;

import java.io.File;

/**
 * 文件存储,用于过多文件
 * @author 子苏
 *
 */
public class SFile {
	
	char[] path;
	
	public SFile(String file) {
		path = file.toCharArray();
	}
	
	
	
	public File getFile() {
		return new File(new String(path));
	}

}
