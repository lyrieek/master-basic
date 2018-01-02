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

	public static void main(String[] args) {
		final Token token = new Token();
		for (int i = 0; i < 2373; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						synchronized (token) {
							System.out.println(Thread.currentThread().getName() + ":" + token.useKey(token.createKey()));
							System.out.println(Thread.currentThread().getName() + ":" + token.useKey(token.createKey()));
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}).start();
		}
	}

	public synchronized String createKey() throws Exception {
		//源获取
		int source = Math.abs(Double.toHexString(Math.random()).hashCode() << 36);
		if (source < 1) {
			//失效源重读
			return createKey();
		}
		//结果空间初始化
		char[] result = new char[(source + "" + System.nanoTime()).length() * 2];
		Random random = new Random();
		for (int i = result.length - 1; i > 0; i -= 2) {
			result[i] = (char) ((random.nextInt(26) + 'a'));
			if (new Random().nextInt(10) == 1) {
				result[i - 1] = (source+"").charAt(0);
				continue;
			}
			result[i - 1] = (char) ((random.nextInt(26) + 'A'));
		}
		result[result.length-1] = new Random().nextInt(100) == 1 ? '/' : '-';
		return key = new String(result);
	}

	public synchronized String getKey() {
		return key;
	}

	public synchronized boolean useKey(String input) {
		System.out.println(key);
		boolean res = input.equals(key);
		key = "";
		if (!res) {
			System.err.println("!!!");
			System.exit(0);
		}
		return res;
	}

}
