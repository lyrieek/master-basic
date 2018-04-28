package pers.th.util.text;

import java.util.Random;

/**
 * 令牌
 * 
 * @author 天浩
 *
 */
public class Token {

	private String key;

	public synchronized String createKey() throws Exception {
		//源获取
		String source = Double.toHexString(Math.random());
		if (source.length() == 0) {
			//失效源重读
			return createKey();
		}
		//结果空间初始化
		char[] result = new char[(source + System.nanoTime()).length() * 2];
		Random random = new Random();
		for (int i = result.length - 1; i > 0; i -= 2) {
			result[i] = (char) ((random.nextInt(26) + 'a'));
			if (new Random().nextInt(3) == 1) {
				//参入数字
				result[i - 1] = source.charAt(random.nextInt(source.length()));
				continue;
			}
			result[i - 1] = (char) ((random.nextInt(26) + 'A'));
		}
		result[result.length-1] = random.nextInt(100) == 1 ? '/' : '-';
		return key = new String(result);
	}

	public synchronized String key() {
		return key;
	}

	public synchronized boolean useKey(String input) {
		boolean res = input.equals(key());
		key = "";
		return res;
	}

}
