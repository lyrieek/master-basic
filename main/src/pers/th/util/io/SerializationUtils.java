package pers.th.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具
 */
public class SerializationUtils {

	/**
	 * Serialize the given object to a byte array.
	 * 
	 * @param object
	 *            the object to serialize
	 * @return an array of bytes representing the object in a portable fashion
	 */
	public static byte[] serialize(final Object object) {
		if (object == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.close(baos);
		}
		return baos.toByteArray();
	}

	/**
	 * Deserialize the byte array into an object.
	 * 
	 * @param bytes
	 *            a serialized object
	 * @return the result of deserializing the bytes
	 */
	public static Object deserialize(final byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		ObjectInputStream ois = null;
		try {
			return (ois = new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.close(ois);
		}
	}

}
