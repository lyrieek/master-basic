package pers.th.util;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * 验证
 * @author 天浩
 *
 */
public class Validate {

    private static final String NULL_MESSAGE = "The validated object is null";
    private static final String EXCLUSIVE_BETWEEN_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
    private static final String NOT_EMPTY_ARRAY_MESSAGE = "The validated array is empty";
    private static final String EMPTY_CHAR_SEQUENCE_MESSAGE = "The validated character sequence is empty";
    private static final String EMPTY_COLLECTION_MESSAGE = "The validated collection is empty";
    private static final String EMPTY_MAP_MESSAGE = "The validated map is empty";
    private static final String TYPE_ERROR_MESSAGE = "Expected type: %s, actual: %s";
    private static final String FILE_NOT_FIND = "file not find:%s";

    public Validate() {
        super();
    }

    public static void notNull(final String msg, final Object... obj) {
        notNull(obj, msg);
        for (Object item : obj) {
            notNull(item, msg);
        }
    }

    public static <T> T notNull(final T object) {
        return notNull(object, NULL_MESSAGE);
    }

    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }

    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        if (array == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }

    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, NOT_EMPTY_ARRAY_MESSAGE);
    }

    public static <T extends Collection<?>> T notEmpty(final T collection, final String message,
                                                       final Object... values) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, EMPTY_COLLECTION_MESSAGE);
    }

    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        if (map == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    public static <T extends Map<?, ?>> T notEmpty(final T map) {
        return notEmpty(map, EMPTY_MAP_MESSAGE);
    }

    public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T notEmpty(final T chars) {
        return notEmpty(chars, EMPTY_CHAR_SEQUENCE_MESSAGE);
    }

    public static void between(final long start, final long end, final long value, final String message) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void between(final T start, final T end, final Comparable<T> value) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(EXCLUSIVE_BETWEEN_MESSAGE, value, start, end));
        }
    }

    public static <T> void between(final T start, final T end, final Comparable<T> value, final String message,
                                   final Object... values) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void between(final long start, final long end, final long value) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(EXCLUSIVE_BETWEEN_MESSAGE, value, start, end));
        }
    }

    public static File tryFile(String path) {
        try {
            File file =	new File(path);
            if (file.exists()) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException(String.format(FILE_NOT_FIND, path));
    }

    public static void typeOf(final Class<?> type, final Object obj) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(
                    String.format(TYPE_ERROR_MESSAGE, type.getName(), obj == null ? "null" : obj.getClass().getName()));
        }
    }

}
