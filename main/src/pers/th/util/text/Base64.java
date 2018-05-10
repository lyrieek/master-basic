package pers.th.util.text;

/**
 * 受Apache-Commons-codec启发,简化其结构
 * 
 * @author 天浩
 *
 */
public class Base64 {

	private static final int MASK_6BITS = 0x3f;

	private static final int MASK_8BITS = 0xff;

	private final int encodeSize = 4;

	private final int decodeSize = encodeSize - 1;

	private static final byte PAD = '=';

	private static final byte[] ENCODE_TABLE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

	private static final byte[] DECODE_TABLE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62,
			-1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28,
			29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

	static class Context {
		int workArea;
		byte[] buffer;
		int pos;
		int readPos;
		boolean eof;
		int currentLinePos;
		int modulus;
	}

	private byte[] ensureBufferSize(final int size, final Context context) {
		if ((context.buffer == null) || (context.buffer.length < context.pos + size)) {
			if (context.buffer == null) {
				context.buffer = new byte[8192];
				context.pos = 0;
				context.readPos = 0;
			} else {
				final byte[] b = new byte[context.buffer.length * 2];
				System.arraycopy(context.buffer, 0, b, 0, context.buffer.length);
				context.buffer = b;
			}
			return context.buffer;
		}
		return context.buffer;
	}

	int readResults(final byte[] b, final int bAvail, final Context context) {
		if (context.buffer != null) {
			final int len = Math.min(context.buffer != null ? context.pos - context.readPos : 0, bAvail);
			System.arraycopy(context.buffer, context.readPos, b, 0, len);
			context.readPos += len;
			if (context.readPos >= context.pos) {
				context.buffer = null;
			}
			return len;
		}
		return context.eof ? -1 : 0;
	}

	/**
	 * 解码
	 * @param pArray
	 * @return
	 */
	public byte[] decode(byte[] pArray) {
		final Context context = new Context();
		decode(pArray, pArray.length, context);
		decode(pArray, -1, context);
		final byte[] result = new byte[context.pos];
		readResults(result, result.length, context);
		return result;
	}

	
	/**
	 * 编码
	 * @param pArray
	 * @return
	 */
	public byte[] encode(byte[] pArray) {
		final Context context = new Context();
		encode(pArray, pArray.length, context);
		encode(pArray, -1, context);
		final byte[] buf = new byte[context.pos - context.readPos];
		readResults(buf, buf.length, context);
		return buf;
	}

	public static boolean isBase64(byte octet) {
		return octet == PAD || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
	}

	public static boolean isBase64(String base64) {
		final byte[] arrayOctet = base64.getBytes();
		for (int i = 0; i < arrayOctet.length; i++) {
			if (!isBase64(arrayOctet[i]) && !Byte.toString(arrayOctet[i]).matches("\\s")) {
				return false;
			}
		}
		return true;
	}

	void encode(final byte[] in, final int inAvail, final Context context) {
		int inPos = 0;
		if (context.eof) {
			return;
		}
		if (inAvail < 0) {
			context.eof = true;
			if (0 == context.modulus) {
				return;
			}
			final byte[] buffer = ensureBufferSize(encodeSize, context);
			final int savedPos = context.pos;
			switch (context.modulus) {
			case 1:
				buffer[context.pos++] = ENCODE_TABLE[(context.workArea >> 2) & MASK_6BITS];
				buffer[context.pos++] = ENCODE_TABLE[(context.workArea << 4) & MASK_6BITS];
				buffer[context.pos++] = PAD;
				buffer[context.pos++] = PAD;
				break;
			case 2:
				buffer[context.pos++] = ENCODE_TABLE[(context.workArea >> 10) & MASK_6BITS];
				buffer[context.pos++] = ENCODE_TABLE[(context.workArea >> 4) & MASK_6BITS];
				buffer[context.pos++] = ENCODE_TABLE[(context.workArea << 2) & MASK_6BITS];
				buffer[context.pos++] = PAD;
				break;
			default:
				throw new IllegalStateException("Impossible modulus " + context.modulus);
			}
			context.currentLinePos += context.pos - savedPos;
		} else {
			for (int i = 0; i < inAvail; i++) {
				final byte[] buffer = ensureBufferSize(encodeSize, context);
				context.modulus = (context.modulus + 1) % 3;
				int b = in[inPos++];
				if (b < 0) {
					b += 256;
				}
				context.workArea = (context.workArea << 8) + b;
				if (0 == context.modulus) {
					buffer[context.pos++] = ENCODE_TABLE[(context.workArea >> 18) & MASK_6BITS];
					buffer[context.pos++] = ENCODE_TABLE[(context.workArea >> 12) & MASK_6BITS];
					buffer[context.pos++] = ENCODE_TABLE[(context.workArea >> 6) & MASK_6BITS];
					buffer[context.pos++] = ENCODE_TABLE[context.workArea & MASK_6BITS];
					context.currentLinePos += 4;
				}
			}
		}
	}

	void decode(final byte[] in, final int inAvail, final Context context) {
		int inPos = 0;
		if (context.eof) {
			return;
		}
		if (inAvail < 0) {
			context.eof = true;
		}
		for (int i = 0; i < inAvail; i++) {
			final byte[] buffer = ensureBufferSize(decodeSize, context);
			final byte b = in[inPos++];
			if (b == PAD) {
				context.eof = true;
				break;
			} else {
				if (b >= 0 && b < DECODE_TABLE.length) {
					final int result = DECODE_TABLE[b];
					if (result >= 0) {
						context.modulus = (context.modulus + 1) % 4;
						context.workArea = (context.workArea << 6) + result;
						if (context.modulus == 0) {
							buffer[context.pos++] = (byte) ((context.workArea >> 16) & MASK_8BITS);
							buffer[context.pos++] = (byte) ((context.workArea >> 8) & MASK_8BITS);
							buffer[context.pos++] = (byte) (context.workArea & MASK_8BITS);
						}
					}
				}
			}
		}

		if (context.eof && context.modulus != 0) {
			final byte[] buffer = ensureBufferSize(decodeSize, context);
			switch (context.modulus) {
			case 1:
				break;
			case 2:
				context.workArea = context.workArea >> 4;
				buffer[context.pos++] = (byte) ((context.workArea) & MASK_8BITS);
				break;
			case 3:
				context.workArea = context.workArea >> 2;
				buffer[context.pos++] = (byte) ((context.workArea >> 8) & MASK_8BITS);
				buffer[context.pos++] = (byte) ((context.workArea) & MASK_8BITS);
				break;
			default:
				throw new IllegalStateException("Impossible modulus " + context.modulus);
			}
		}
	}

	/**
	 * 字符串编码
	 * @param binaryData
	 * @return
	 */
	public static String encodeBase64(String binaryData) {
		return new String(new Base64().encode(binaryData.getBytes()));
	}

	/**
	 * 解码为字符串
	 * @param base64Data
	 * @return
	 */
	public static String decodeBase64(String base64Data) {
		return new String(new Base64().decode(base64Data.getBytes()));
	}
}
