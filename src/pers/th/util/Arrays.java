package pers.th.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Arrays {

	public static final char[] EMPTY_CHARS = new char[0];

	public <T> List<T> asList(@SuppressWarnings("unchecked") T... param) {
		if (param != null) {
			return java.util.Arrays.asList(param);
		}
		return new ArrayList<T>();
	}

	/**
	 * 是否全部都是null
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

	/**
	 * 是否包含null,或者本身就是null
	 * 
	 * @param array
	 * @return
	 */
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
		c = reverse(c);
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
		}
	}

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

	public static void join() {

	}

	public static int getLength(final Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

}
