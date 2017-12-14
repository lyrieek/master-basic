package pers.th.util.text;

import pers.th.util.Arrays;
import pers.th.util.SystemInfo;

public class XStrings {

	/**
	 * ���д�ɢ���Ҹ�ʽ��ÿһ��
	 * @param source Դ
	 * @param target ���滻����
	 * @param replace �滻�ı�
	 * @return
	 */
	public static String[] lines(String source, String target, String replace) {
		String[] allLine = lines(source);
		for (int i = 0; i < allLine.length; i++) {
			allLine[i] = allLine[i].replace(target, replace);
		}
		return allLine;
	}

	/**
	 * �����з���ɢ�ַ���
	 */
	public static String[] lines(String source) {
		return source.split(SystemInfo.LINE_SEPARATOR);
	}
	
	/**
     * <pre>
	 * isEmpty(null);   true
	 * isEmpty("");     true
     * isEmpty(" ")     false
     * isEmpty("a")     false
     * </pre>
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isEmpty(CharSequence source) {
		return source == null || source.length() == 0;
	}
	
	public static boolean hasEmpty(CharSequence... source) {
		if (Arrays.hasEmpty(source)) {
			return true;
		}
		return false;
	}
	
	/**
	 * !isEmpty
	 * @param source
	 * @return
	 */
	public static boolean notEmpty(CharSequence source) {
		return source != null && source.length() > 0;
	}

	/**
	 * ��ת���е��ַ���
	 * @param param
	 * @return
	 */
	public static String reverse(final String param) {
		char[] value = param.toCharArray();
		final int len = value.length;
		for (int i = 0; i < len / 2; i++) {
			char temp = value[i];
			value[i] = value[len - 1 - i];
			value[len - 1 - i] = temp;
		}
		return new String(value);
	}
	
	public void filter() {
		
	}
	

}
