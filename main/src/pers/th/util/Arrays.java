package pers.th.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pers.th.util.text.StrBuffer;

/**
 * @author lyrieek
 *
 */
public class Arrays {

	public static final char[] EMPTY_CHARS = new char[0];

	public <T> List<T> asList(T... param) {
		if (param != null && param.length > 0) {
			return java.util.Arrays.asList(param);
		}
		return new ArrayList<>();
	}

	public <T> T[] asList(List<T> param) {
		if (param != null && param.size() > 0) {
			return param.toArray((T[]) param.toArray());
		}
		return (T[]) new ArrayList<T>().toArray();
	}
	
	

	/**
	 *
	 * @param array
	 * @return
	 */
	public static boolean anyEmpty(Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		if (array[0] != null) {
			return false;
		}
		for (int i = 1; i < array.length; i++) {
			if (array[i] != null) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasEmpty(final Object[] array) {
		if (getLength(array) == 0) {
			return true;
		}
		for (Object object : array) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Character[] c = new Character[] { 'a', 'b', 'c', 'd' };
		for (Character item : reverse(c)) {
			System.out.println(item);
		}
	}

	/**
	 * @param array
	 * @return
	 */
	public static <T> T[] reverse(final T[] array) {
		if (array == null) {
			return null;
		}
		final int len = array.length;
		for (int i = 0; i < len / 2; i++) {
			T temp = array[i];
			array[i] = array[len - 1 - i];
			array[len - 1 - i] = temp;
		}
		return array;
	}

	/**
	 * @param strs
	 * @return
	 */
	public static String join(List<String> strs) {
		StrBuffer buffer = new StrBuffer(strs.size());
		for (String item : strs) {
			buffer.append(item);
		}
		return buffer.toString();
	}

	/**
	 * @param array
	 * @return
	 */
	public static int getLength(final Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

	/**
	 * close Closeable
	 * @param cArr
	 */
	public static void close(Closeable... cArr) {
		if (cArr == null) {
			return;
		}
		try {
			for (Closeable closeable : cArr) {
				if (closeable != null) {
					closeable.close();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("can't close", e);
		}
	}

}
