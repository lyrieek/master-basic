package pers.th.util.text;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import pers.th.util.Arrays;
import pers.th.util.SystemInfo;

/**
 * 代替StringBuffer
 * @author 天浩
 */
public class StrBuffer implements CharSequence, Appendable, Serializable {
	private static final long serialVersionUID = 628L;

	protected char[] value;
	protected int offset = 0;

	/**
	 * real data size
	 */
	protected int size;

	public StrBuffer(int size) {
		value = new char[size];
	}

	public StrBuffer() {
		this(36);
	}

	public StrBuffer(final String str) {
		value = new char[str.length() * 3 / 2];
		append(str);
	}

	/**
	 * 在现有文本之前插入文本
	 * 
	 * @param str
	 * @return
	 */
	public StrBuffer after(String str) {
		if (str == null || str.trim().length() == 0) {
			return this;
		}
		dilatation(str.length());
		System.out.println(value.length + ":" + str.length());
		System.arraycopy(value, 0, value, str.length(), size);
		System.arraycopy(str.toCharArray(), 0, value, 0, str.length());
		size += str.length();
		return this;
	}

	/**
	 * <pre>
	 * 包裹文本:
	 * 	"1".wrap("2"); 212
	 * 	"abc".wrap("123"); 321abc123
	 * </pre>
	 * @param str
	 * @return
	 */
	public StrBuffer wrap(String str) {
		after(XStrings.reverse(str));
		dilatation(str.length());
		str.getChars(0, str.length(), value, size);
		size += str.length();
		return this;
	}

	/**
	 * 移动real size索引的位置
	 * @param index
	 */
	public void move(int index) {
		size = index;
	}

	public StrBuffer reverse() {
		minimizeCapacity();
		final int len = value.length;
		for (int i = 0; i < len / 2; i++) {
			char temp = value[i];
			value[i] = value[len - 1 - i];
			value[len - 1 - i] = temp;
		}
		return this;
	}

	/**
	 * 检查自动扩容,默认扩充capacity的三分之一
	 * 
	 * @param capacity
	 *            给定大小,如果data超过此大小将启动扩容
	 * @return
	 */
	public StrBuffer dilatation(int capacity) {
		capacity = size + capacity;
		if (capacity > value.length) {
			System.arraycopy(value, 0, value = new char[capacity * 3 / 2], 0, size);
		}
		return this;
	}

	/**
	 * 最小化容量,使real size=data length
	 * 
	 */
	public StrBuffer minimizeCapacity() {
		if (value.length > size) {
			final char[] old = value;
			value = new char[size];
			System.arraycopy(old, 0, value, 0, size);
		}
		return this;
	}

	/**
	 * 设置长度,可能会发生扩容
	 * 
	 * @param length
	 * @return
	 */
	public StrBuffer setLength(final int length) {
		if (length < 0) {
			throw new StringIndexOutOfBoundsException(length);
		}
		dilatation(length - size);
		for (int i = length; i < size; i++) {
			value[i] = ' ';
		}
		size = length;
		return this;
	}
	
	/**
	 * 如果以endString结束,删掉endString
	 * @param endString
	 */
	public String rmEnd(final String endString) {
		if (endsWith(endString)) {
			return substring(0, size - endString.length());
		}
		return toString();
	}
	
	/**
	 * 如果以startString开始,删掉startString
	 * @param startString
	 */
	public String rmStart(final String startString) {
		if (startsWith(startString)) {
			return substring(startString.length());
		}
		return toString();
	}

	@Override
	public char charAt(final int index) {
		if (validateIndex(index)) {
			return value[index];
		}
		throw new StringIndexOutOfBoundsException(index);
	}

	public StrBuffer set(final int index, final char ch) {
		if (validateIndex(index)) {
			value[index] = ch;
		}
		return this;
	}

	public StrBuffer deleteCharAt(final int index) {
		if (validateIndex(index)) {
			delete(index, index + 1, 1);
			return this;
		}
		throw new StringIndexOutOfBoundsException(index);
	}

	public char[] toCharArray() {
		if (size == 0) {
			return Arrays.EMPTY_CHARS;
		}
		final char chars[] = new char[size];
		System.arraycopy(value, 0, chars, 0, size);
		return chars;
	}

	public char[] toCharArray(final int begin, int end) {
		if (size == 0) {
			return Arrays.EMPTY_CHARS;
		}
		final char chars[] = new char[(end = validateRange(begin, end)) - begin];
		System.arraycopy(value, begin, chars, 0, chars.length);
		return chars;
	}

	public char[] getChars(char[] destination) {
		if (destination == null || destination.length < size) {
			destination = new char[size];
		}
		System.arraycopy(value, 0, destination, 0, size);
		return destination;
	}

	public StrBuffer appendNewLine() {
		return append(SystemInfo.LINE_SEPARATOR);
	}

	public StrBuffer append(Object obj) {
		if (obj == null) {
			return this;
		}
		if (obj instanceof StrBuffer) {
			return append((StrBuffer) obj);
		}
		return append(obj.toString());
	}

	public StrBuffer append(final CharSequence seq) {
		if (seq instanceof StrBuffer) {
			return append((StrBuffer) seq);
		}
		return append(seq.toString());
	}

	public StrBuffer append(final CharSequence seq, final int begin, final int length) {
		return append(seq.toString(), begin, length);
	}

	public StrBuffer append(final String str) {
		if (str == null) {
			return this;
		}
		final int strLen = str.length();
		dilatation(strLen);
		str.getChars(0, strLen, value, size);
		size += strLen;
		return this;
	}

	public StrBuffer append(final String str, final int begin, final int length) {
		if (begin < 0 || begin + length > str.length() || length <= 0) {
			throw new StringIndexOutOfBoundsException(
					String.format("Index & length must be valid:%s,%d,%d", str, begin, length));
		}
		dilatation(length);
		str.getChars(begin, begin + length, value, size);
		size += length;
		return this;
	}

	public StrBuffer append(final StrBuffer str) {
		return append(str.value, 0, str.length());
	}

	public StrBuffer append(final char[] chars) {
		return append(chars, 0, chars.length);
	}

	public StrBuffer append(final char[] chars, final int begin, final int length) {
		if (begin < 0 || begin > chars.length || length < 0 || (begin + length) > chars.length || length < 0) {
			throw new StringIndexOutOfBoundsException(
					String.format("Invalid append:chars:%s,beginIndex:%d,length:%d", new String(chars), begin, length));
		}
		System.arraycopy(chars, begin, dilatation(length).value, size, length);
		size += length;
		return this;
	}

	public StrBuffer append(final char ch) {
		dilatation(1).value[size++] = ch;
		return this;
	}

	public StrBuffer appendLine(final String str) {
		return append(str).appendNewLine();
	}

	public StrBuffer appendAll(final Object... array) {
		for (final Object element : array) {
			append(element);
		}
		return this;
	}

	public StrBuffer appendAll(final Iterator<?> it) {
		while (it.hasNext()) {
			append(it.next());
		}
		return this;
	}

	public StrBuffer appendWithSeparators(final Object[] array, final String separator) {
		if (array != null && array.length > 0) {
			final String sep = Objects.toString(separator, "");
			append(array[0]);
			for (int i = 1; i < array.length; i++) {
				append(sep);
				append(array[i]);
			}
		}
		return this;
	}

	public StrBuffer appendWithSeparators(final Iterator<?> it, final String separator) {
		if (it != null) {
			final String sep = Objects.toString(separator, "");
			while (it.hasNext()) {
				append(it.next());
				if (it.hasNext()) {
					append(sep);
				}
			}
		}
		return this;
	}

	/**
	 * 添加count个padChar字符
	 * 
	 * @param padChar
	 *            字符
	 * @param count
	 *            添加数量
	 */
	public StrBuffer append(final char padChar, final int count) {
		if (count > 0) {
			dilatation(count);
			for (int i = 0; i < count; i++) {
				value[size++] = padChar;
			}
		}
		return this;
	}

	public StrBuffer insert(final int index, String str) {
		if (validateIndex(index) && str != null) {
			final int strLen = str.length();
			if (strLen > 0) {
				dilatation(strLen);
				System.arraycopy(value, index, value, index + strLen, size - index);
				size += strLen;
				System.arraycopy(str.toCharArray(), 0, value, index, strLen);
			}
		}
		return this;
	}

	public StrBuffer insert(final int index, final char chars[], final int offset, final int length) {
		if (validateIndex(index) || offset < 0 || offset > chars.length || length < 0 || offset + length > chars.length
				|| length < 0) {
			throw new StringIndexOutOfBoundsException(String.format(
					"Invalid insert:chars:%s,index:%d,offset:%d,length:%d", new String(chars), index, offset, length));
		}
		dilatation(length);
		System.arraycopy(value, index, value, index + length, size - index);
		System.arraycopy(chars, offset, value, index, length);
		size += length;
		return this;
	}

	public StrBuffer delete(final int begin, final int end, final int length) {
		System.arraycopy(value, end, value, begin, size - end);
		size -= length;
		return this;
	}

	public StrBuffer delete(final int begin, int end) {
		return delete(begin, end = validateRange(begin, end), end - begin);
	}

	public StrBuffer deleteAll(final char ch) {
		for (int i = 0; i < size; i++) {
			if (value[i] == ch) {
				final int start = i;
				while (++i < size) {
					if (value[i] != ch) {
						break;
					}
				}
				final int len = i - start;
				delete(start, i, len);
				i -= len;
			}
		}
		return this;
	}

	public StrBuffer deleteFirst(final char ch) {
		for (int i = 0; i < size; i++) {
			if (value[i] == ch) {
				delete(i, i + 1, 1);
				break;
			}
		}
		return this;
	}

	public StrBuffer deleteAll(final String str) {
		int index = indexOf(str, 0);
		while (index != -1) {
			delete(index, index + str.length(), str.length());
			index = indexOf(str, index);
		}
		return this;
	}

	public StrBuffer deleteFirst(final String str) {
		final int len = (str == null ? 0 : str.length());
		if (len > 0) {
			final int index = indexOf(str, 0);
			if (index >= 0) {
				delete(index, index + len, len);
			}
		}
		return this;
	}

	public void replace(final int begin, final int end, final int rmLength,
			final String insert, final int insertLength) {
		final int newSize = size - rmLength + insertLength;
		if (insertLength != rmLength) {
			dilatation(insertLength - rmLength);
			System.arraycopy(value, end, value, begin + insertLength, size - end);
			size = newSize;
		}
		if (insertLength > 0) {
			insert.getChars(0, insertLength, value, begin);
		}
	}

	public StrBuffer replace(final int begin, int end, final String replace) {
		end = validateRange(begin, end);
		final int insertLen = (replace == null ? 0 : replace.length());
		replace(begin, end, end - begin, replace, insertLen);
		return this;
	}

	/**
	 * 
	 * @param search
	 *            查询的char
	 * @param replace
	 *            替换的char
	 * @param isAll
	 *            是否替换所有的char
	 * @return
	 */
	public StrBuffer replace(final char search, final char replace, boolean isAll) {
		if (search == replace) {
			return this;
		}
		for (int i = 0; i < size; i++) {
			if (value[i] == search) {
				value[i] = replace;
				if (!isAll) {
					break;
				}
			}
		}
		return this;
	}

	public StrBuffer replaceAll(final String search, final String replace) {
		final int length = (search == null ? 0 : search.length());
		if (length > 0) {
			final int replaceLen = (replace == null ? 0 : replace.length());
			int index = indexOf(search, 0);
			while (index >= 0) {
				replace(index, index + length, length, replace, replaceLen);
				index = indexOf(search, index + replaceLen);
			}
		}
		return this;
	}

	public StrBuffer replaceFirst(final String search, final String replace) {
		final int searchLen = (search == null ? 0 : search.length());
		if (searchLen > 0) {
			final int index = indexOf(search, 0);
			if (index >= 0) {
				final int replaceLen = (replace == null ? 0 : replace.length());
				replace(index, index + searchLen, searchLen, replace, replaceLen);
			}
		}
		return this;
	}

	/**
	 * 清空字符串头尾空白部分,并且最小化字符串
	 * 
	 * @return
	 */
	public StrBuffer trim() {
		if (size == 0) {
			return this;
		}
		int len = size;
		final char[] buf = value;
		int pos = 0;
		while (pos < len && buf[pos] <= ' ') {
			pos++;
		}
		while (pos < len && buf[len - 1] <= ' ') {
			len--;
		}
		if (len < size) {
			delete(len, size);
		}
		if (pos > 0) {
			delete(0, pos);
		}
		return minimizeCapacity();
	}

	/**
	 * @param offset 最终获取的起始位置:default 0
	 * @return
	 */
	public StrBuffer setOffset(final int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * @return 最终获取的起始位置:默认0
	 */
	public int offset() {
		return offset;
	}

	public boolean startsWith(final String str) {
		if (str == null) {
			return false;
		}
		final int len = str.length();
		if (len == 0) {
			return true;
		}
		if (len > size) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (value[i] != str.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	public boolean endsWith(final String str) {
		if (str == null) {
			return false;
		}
		final int len = str.length();
		if (len == 0) {
			return true;
		}
		if (len > size) {
			return false;
		}
		int pos = size - len;
		for (int i = 0; i < len; i++, pos++) {
			if (value[pos] != str.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public CharSequence subSequence(final int begin, final int end) {
		return substring(begin, validateRange(begin, end));
	}

	/**
	 * setOffset(start)拥有更高的效率
	 * 
	 * @param begin
	 * @return
	 */
	public String substring(final int begin) {
		return substring(begin, size);
	}

	public String substring(final int begin, final int end) {
		return new String(value, begin, end - begin);
	}

	public String left(final int length) {
		if (length <= 0) {
			return "";
		}
		return new String(value, 0, length);
	}

	public String right(final int length) {
		if (length <= 0) {
			return "";
		} else if (length >= size) {
			return new String(value, 0, size);
		}
		return new String(value, size - length, length);
	}

	public boolean contains(final char ch) {
		final char[] thisBuf = value;
		for (int i = 0; i < size; i++) {
			if (thisBuf[i] == ch) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(final String str) {
		return indexOf(str, 0) >= 0;
	}

	public int indexOf(final char ch, int begin) {
		begin = (begin < 0 ? 0 : begin);
		if (begin >= size) {
			return -1;
		}
		final char[] thisBuf = value;
		for (int i = begin; i < size; i++) {
			if (thisBuf[i] == ch) {
				return i;
			}
		}
		return -1;
	}

	public int indexOf(final String str) {
		return indexOf(str, 0);
	}

	/**
	 * 查找字符串
	 * 
	 * @param str
	 *            查找的字符串
	 * @param index
	 *            开始查找的索引
	 * @return 它的索引,找不到则返回-1
	 */
	public int indexOf(final String str, int index) {
		index = (index < 0 ? 0 : index);
		if (str == null || index >= size) {
			return -1;
		}
		final int strLen = str.length();
		if (strLen == 1) {
			return indexOf(str.charAt(0), index);
		}
		if (strLen == 0) {
			return index;
		}
		if (strLen > size) {
			return -1;
		}
		final char[] thisBuf = value;
		final int len = size - strLen + 1;
		outer: for (int i = index; i < len; i++) {
			for (int j = 0; j < strLen; j++) {
				if (str.charAt(j) != thisBuf[i + j]) {
					continue outer;
				}
			}
			return i;
		}
		return -1;
	}

	public int lastIndexOf(final char ch) {
		return lastIndexOf(ch, size - 1);
	}

	public int lastIndexOf(final char ch, int index) {
		index = (index >= size ? size - 1 : index);
		if (index < 0) {
			return -1;
		}
		for (int i = index; i >= 0; i--) {
			if (value[i] == ch) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(final String str) {
		return lastIndexOf(str, size - 1);
	}

	public int lastIndexOf(final String str, int index) {
		final int strLen = str.length();
		if (strLen > 0 && strLen <= size) {
			if (strLen == 1) {
				return lastIndexOf(str.charAt(0), index);
			}

			outer: for (int i = index - strLen + 1; i >= 0; i--) {
				for (int j = 0; j < strLen; j++) {
					if (str.charAt(j) != value[i + j]) {
						continue outer;
					}
				}
				return i;
			}

		} else if (strLen == 0) {
			return index;
		}
		return -1;
	}

	public void appendTo(final Appendable appendable) throws IOException {
		if (appendable instanceof StringBuilder) {
			((StringBuilder) appendable).append(value, 0, size);
		} else if (appendable instanceof StringBuffer) {
			((StringBuffer) appendable).append(value, 0, size);
		} else {
			appendable.append(this);
		}
	}

	public boolean equalsIgnoreCase(final StrBuffer compare) {
		if (this == compare) {
			return true;
		}
		if (this.size != compare.size) {
			return false;
		}
		for (int i = size - 1; i >= 0; i--) {
			if (value[i] != compare.value[i]
					&& Character.toUpperCase(value[i]) != Character.toUpperCase(compare.value[i])) {
				return false;
			}
		}
		return true;
	}

	public int validateRange(final int begin, int end) {
		if (begin < 0) {
			throw new StringIndexOutOfBoundsException(begin);
		}
		if (end > size) {
			end = size;
		}
		if (begin > end) {
			throw new StringIndexOutOfBoundsException("begin>end:" + begin + "," + end);
		}
		return end;
	}

	public boolean validateIndex(final int index) {
		return (index > 0 && index < size);
	}

	public boolean equals(final StrBuffer other) {
		if (this == other) {
			return true;
		}
		if (other == null || this.size != other.size) {
			return false;
		}
		for (int i = size - 1; i >= 0; i--) {
			if (value[i] != other.value[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof StrBuffer && equals((StrBuffer) obj);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = size - 1; i >= 0; i--) {
			hash = 31 * hash + value[i];
		}
		return hash;
	}

	/**
	 * data real size
	 * 
	 * @return
	 */
	@Override
	public int length() {
		return size;
	}

	public int capacity() {
		return value.length;
	}

	@Override
	public String toString() {
		return new String(value, offset, size);
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isBlack() {
		if (isEmpty()) {
			return true;
		}
		for (char c : value) {
			if (c != ' ') {
				return false;
			}
		}
		return true;
	}

	public StrBuffer clear() {
		value = new char[36];
		size = 0;
		return this;
	}

	public StringBuffer toStringBuffer() {
		return new StringBuffer(size).append(value, offset, size);
	}

	public StringBuilder toStringBuilder() {
		return new StringBuilder(size).append(value, offset, size);
	}

}
