package pers.th.util.io;

import java.io.File;

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
