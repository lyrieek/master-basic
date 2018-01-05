package pers.th.util.math;

public class Number {

	public static StringBuffer analysis(String a, String b) {
		char[] as = a.toCharArray();
		char[] bs = b.toCharArray();
		int lastBsIndex = bs.length - 1;
		StringBuffer result = new StringBuffer();
		int storage = 0;
		for (int i = as.length - 1; i > -1; i--) {
			// System.out.println(as[i]);
			int endInt = 0;
			if (lastBsIndex > -1) {
				endInt = charToInt(bs[lastBsIndex]);
			}
			int appendNumber = charToInt(as[i]) + endInt + storage;
			if (appendNumber >= 10) {
				storage = (appendNumber / 10);
				appendNumber %= 10;
			} else {
				storage = 0;
			}
			result.append(appendNumber);
			lastBsIndex--;
		}
		for (; lastBsIndex > -1; lastBsIndex--) {
			int appendNumber = charToInt(bs[lastBsIndex]) + storage;
			if (appendNumber >= 10) {
				storage = (appendNumber / 10);
				appendNumber %= 10;
			} else {
				storage = 0;
			}
			result.append(appendNumber);
		}
		if (storage != 0) {
			result.append(storage);
		}
		return result.reverse();
	}

	private static int charToInt(char in) {
		if (in < '0' || in > '9') {
			throw new RuntimeException("char to int exception:" + in + " not is number");
		}
		return (int) in - 48;
	}
}
