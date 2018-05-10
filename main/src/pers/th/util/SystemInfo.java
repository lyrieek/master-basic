package pers.th.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

/**
 * 系统信息
 * @author 天浩
 *
 */
public class SystemInfo {

	/**
	 * 文件编码
	 */
	public static final String FILE_ENCODING = prop("file.encoding");

	/**
	 * 文件路径分隔符,LINUX/UNIX:"/";Windows:"\"
	 */
	public static final String FILE_SEPARATOR = prop("file.separator");

	public static final String AWT_FONTS = prop("java.awt.fonts");

	public static final String CLASS_PATH = prop("java.class.path");

	public static final String CLASS_VERSION = prop("java.class.version");

	public static final String COMPILER = prop("java.compiler");

	public static final String HOME = prop("java.home");

	public static final String TEMP_DIR = prop("java.io.tmpdir");

	public static final String LIBRARY_PATH = prop("java.library.path");

	public static final String RUNTIME_NAME = prop("java.runtime.name");

	public static final String RUNTIME_VERSION = prop("java.runtime.version");

	public static final String VENDOR = prop("java.vendor");

	public static final String VENDOR_URL = prop("java.vendor.url");

	public static final String VERSION = prop("java.version");

	public static final String LINE_SEPARATOR = prop("line.separator");

	public static final String OS_NAME = prop("os.name");

	public static final String OS_VERSION = prop("os.version");

	public static final String PATH_SEPARATOR = prop("path.separator");

	public static final String USER_DIR = prop("user.dir");

	public static final String USER_HOME = prop("user.home");

	public static final String USER_NAME = prop("user.name");

	public static final String USER_LANGUAGE = prop("user.language");

	public static File getJavaHome() {
		return new File(HOME);
	}

	public static String getHostName() {
		return OS_NAME.contains("Windows") ? env("COMPUTERNAME") : env("HOSTNAME");
	}

	/**
	 * 获取临时目录
	 * @return
	 */
	public static File getTempDir() {
		return new File(TEMP_DIR);
	}

	public static File getUserDir() {
		return new File(USER_DIR);
	}

	public static File getUserHome() {
		return new File(USER_HOME);
	}


	/**
	 * 获取系统剪切板内容
	 * @return
	 * @throws UnsupportedFlavorException
	 * @throws IOException
	 */
	public static String getClipboardText() throws UnsupportedFlavorException, IOException {
		Transferable clipTf = getSystemClipboard().getContents(null);
		if (clipTf != null && clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return clipTf.getTransferData(DataFlavor.stringFlavor).toString();
		}
		return null;
	}

	/**
	 * 设置系统剪切板内容
	 * @param context
	 */
	public static void setClipboardText(String context) {
		getSystemClipboard().setContents(new StringSelection(context), null);
	}
	
	/**
	 * 获取系统剪切板
	 * @return
	 */
	public static Clipboard getSystemClipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	public static String prop(final String property) {
		try {
			return System.getProperty(property);
		} catch (Exception ex) {
			System.err.println(property + " property cannot get");
			ex.printStackTrace();
			return "";
		}
	}

	public static String env(final String name) {
		try {
			return System.getenv(name);
		} catch (Exception ex) {
			System.err.println(name + " env cannot get");
			ex.printStackTrace();
			return "";
		}
	}

}