package pers.th.util.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public static String reader(String path) {
        return reader(path, Charset.defaultCharset());
    }

    public static String reader(String path, Charset charSet) {
        return reader(XFile.getFile(path), charSet);
    }

    /**
     * 直接读取文件
     *
     * @param file: wait read file
     * @return content
     */
    public static String reader(File file, Charset charSet) {
        if (file == null || !file.isFile()) {
            return "";
        }
        FileInputStream fis = null;
        FileChannel channel;
        String result;
        try {
            fis = new FileInputStream(file);
            channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);
            result = new String(buffer.array(), 0, (int) channel.size(), charSet);
            buffer.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(fis);
        }
        return result;
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            throw new RuntimeException("IOUtils.close(): can't close:" + e);
        }
    }

    public static void close(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public static String toString(InputStream inStream, Charset charset) throws IOException {
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
        int len;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        while ((len = inStream.read(buff)) > 0) {
            byteStream.write(buff, 0, len);
        }
        close(inStream);
        return new String(byteStream.toByteArray(), 0, byteStream.size(), charset);
    }

    public static long copy(final Reader input, final Writer output) throws IOException {
        return copy(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(final Reader input, final Writer output, final char[] buffer) throws IOException {
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void write(String fileOutPath, String context) {
        XFile xfile = new XFile(fileOutPath);
        if (xfile.exists()) {
            xfile.writeAndClose(context);
        }
    }

    public static void append(String fileOutPath, String context) {
        XFile xfile = new XFile(fileOutPath);
        if (xfile.exists()) {
            xfile.writeAndClose(context);
        }
    }

    public static void write(String fileOutPath, String content, Charset charset) {
        XFile xfile = new XFile(fileOutPath, charset);
        try {
            if (xfile.exists()) {
                xfile.delete();
            }
            if (!xfile.createNewFile()) {
                return;
            }
            xfile.writeAndClose(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
