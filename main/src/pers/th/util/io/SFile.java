package pers.th.util.io;

import java.io.File;

/**
 * �ļ��洢,���ڹ����ļ�
 * @author ����
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
