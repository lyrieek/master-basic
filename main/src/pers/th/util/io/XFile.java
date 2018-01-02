package pers.th.util.io;

import java.io.File;

public class XFile extends File{

	private static final long serialVersionUID = 1L;

	public XFile(String pathname) {
		super(pathname);
	}
	
	public static void main(String[] args) {
//		System.out.println(new File("f:\temp\archive\BirtS"));
		System.out.println(new XFile("f:\\temp").rm());
	}
	
	public boolean rm() {
		return rm(this);
	}
	
	public static boolean rm(File file) {
		if (file.isFile()) {
			return file.delete();
		}
		for (File item : file.listFiles()) {
			if (item.isDirectory() && !rm(item)) {
				return false;
			}
			System.out.println(item);
			if (!item.delete()) {
				return false;
			}
		}
		return file.delete();
	}
	
}
