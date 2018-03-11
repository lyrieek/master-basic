package pers.th.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;

/**
 * 简易集合
 * @author 天浩
 */
public class SList<E> extends AbstractList<E> implements RandomAccess, java.io.Serializable {
	private static final long serialVersionUID = -6168330278027606446L;
	private final E[] array;
	
	public SList(E[] arr) {
		if (arr == null)
			throw new NullPointerException();
		array = arr;
	}
	
	public static SList<?> getList() {
		return new SList<>(new Object[]{});
	}

	public int size() {
		return array.length;
	}

	public Object[] toArray() {
		return array.clone();
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		int size = size();
		if (a.length < size)
			return Arrays.copyOf(this.array, size, (Class<? extends T[]>) a.getClass());
		System.arraycopy(this.array, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}

	public E get(int index) {
		return array[index];
	}

	public E set(int index, E element) {
		E oldValue = array[index];
		array[index] = element;
		return oldValue;
	}

	public int indexOf(Object o) {
		if (o == null) {
			for (int i = 0; i < array.length; i++)
				if (array[i] == null)
					return i;
		} else {
			for (int i = 0; i < array.length; i++)
				if (o.equals(array[i]))
					return i;
		}
		return -1;
	}

	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	public E[] getArray() {
		return array;
	}

}
