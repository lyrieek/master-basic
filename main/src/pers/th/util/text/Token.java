package pers.th.util.text;

import java.util.Random;

/**
 * ����
 * 
 * @author ���
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
		//Դ��ȡ
		String source = Double.toHexString(Math.random());
		if (source.length() == 0) {
			//ʧЧԴ�ض�
			return createKey();
		}
		//����ռ��ʼ��
		char[] result = new char[(source + System.nanoTime()).length() * 2];
		Random random = new Random();
		for (int i = result.length - 1; i > 0; i -= 2) {
			result[i] = (char) ((random.nextInt(26) + 'a'));
			if (new Random().nextInt(3) == 1) {
				//��������
				result[i - 1] = source.charAt(random.nextInt(source.length()));
				continue;
			}
			result[i - 1] = (char) ((random.nextInt(26) + 'A'));
		}
		result[result.length-1] = random.nextInt(100) == 1 ? '/' : '-';
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
