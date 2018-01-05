package pers.th.util.math;

public class Number {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		int sum = 0;
//		String sum = "";
		for (int i = 0; i < 1000000; i++) {
//			sum = analysis("22222222", "191991919");
			sum = Integer.parseInt("22222222") + Integer.parseInt("191991919");//214214141
		}
		System.out.println(System.currentTimeMillis() - s);
		System.out.println(sum);
	}
	
	/**
	 * 
	 * 可以计算超long数据,计算小数值速度比普通的计算方式耗时多出三倍
	 * @param a 
	 * @param b
	 * @return
	 */
	public static String add(String a, String b) {
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
		for (int appendNumber; lastBsIndex > -1; lastBsIndex--) {
			appendNumber = charToInt(bs[lastBsIndex]) + storage;
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
		return result.reverse().toString();
	}

	private static int charToInt(char in) {
		if (in < '0' || in > '9') {
			throw new RuntimeException("char to int exception:" + in + " not is number");
		}
		return (int) in - 48;
	}
}
