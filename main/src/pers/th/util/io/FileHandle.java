package pers.th.util.io;

import java.io.File;

/**
 * �����ļ���ʹ�õĻص�
 * @author ���
 *
 */
public abstract class FileHandle {
	
	/**
	 * �����ļ�������
	 * @return
	 */
	public boolean filter(File file) {
		return true;
	}
	
	/**
	 * ������
	 */
	public abstract void run(File file);
	
}
