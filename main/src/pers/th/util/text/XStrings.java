package pers.th.util.text;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pers.th.util.Arrays;
import pers.th.util.SystemInfo;

public class XStrings {

	/**
	 * 将行打散并且格式化每一行
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            待替换正则
	 * @param replace
	 *            替换文本
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
	 * 依据行符打散字符串
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
	
	/**
	 * <pre>
	 * isBlack(null);   true
	 * isBlack("");     true
	 * isBlack(" ")     true
	 * isBlack("a")     false
	 * </pre>
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isBlack(CharSequence source) {
		return source == null || source.toString().trim().length() == 0;
	}

	public static boolean hasEmpty(CharSequence... source) {
		if (Arrays.hasEmpty(source)) {
			return true;
		}
		return false;
	}

	/**
	 * !isEmpty
	 * 
	 * @param source
	 * @return
	 */
	public static boolean notEmpty(CharSequence source) {
		return source != null && source.length() > 0;
	}

	/**
	 * 反转所有的字符串
	 * 
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

	/**
	 * 截取两块文本中的文本
	 * 
	 * <pre>
	 * e.g:
	 * tear("asd asd v19 dc hhh sdf 123 asd v5c", "v", "c", 20) >> 5
	 * tear("asd asd v19 dc hhh sdf 123 asd v5c", "v", "c", 0) >> 19 d
	 * tear("asd asd v19 dc hhh sdf 123 asd v5c", "asd", "dc", 0) >>  asd v19
	 * tear("asd asd v19 dc hhh sdf 123 asd v5c", "sdf", "asd", 0) >>  123
	 * </pre>
	 * 
	 * @param source
	 *            源文本
	 * @param begin
	 *            起点文本
	 * @param end
	 *            结数文本
	 * @param index
	 *            寻找点
	 * @return
	 */
	public static String tear(String source, String begin, String end, int index) {
		int fromIndex = source.indexOf(begin, index) + begin.length();
		return source.substring(fromIndex, source.indexOf(end, fromIndex));
	}

	public static Set<String> getRegexList(String input, String regex) {
		Matcher pattern = Pattern.compile(regex).matcher(input);
		Set<String> list = new HashSet<>();
		while (pattern.find()) {
			list.add(pattern.group());
		}
		return list;
	}

	public void filter() {

	}

	public static String join(String[] split) {
		return Arrays.join(java.util.Arrays.asList(split));
	}

}
