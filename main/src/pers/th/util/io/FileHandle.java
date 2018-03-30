package pers.th.util.io;

import java.io.File;

/**
 * 处理文件集使用的回调
 * @author 天浩
 *
 */
public abstract class FileHandle {
	
	/**
	 * 过滤文件集条件
	 * @return
	 */
	public boolean filter(File file) {
		return true;
	}
	
	/**
	 * 处理函数
	 */
	public abstract void run(File file);
	
}
