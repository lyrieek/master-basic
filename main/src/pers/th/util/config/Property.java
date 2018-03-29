package pers.th.util.config;

import pers.th.util.io.LineReader;

import java.io.*;
import java.util.*;

/**
 * Created by Tianhao on 2018-3-28.
 * <p>
 * replace {@link java.util.Properties}
 */
public class Property extends Hashtable<String, String> {
    protected Property defaults;

    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public Property() {
        this(null);
    }

    public Property(Property defaults) {
        this.defaults = defaults;
    }

    public Object setProperty(String key, String value) {
        return put(key, value);
    }

    public void load(LineReader lr) throws IOException {
        char[] convtBuf = new char[1024];
        int limit;
        int keyLen;
        int valueStart;
        char c;
        boolean hasSep;
        boolean precedingBackslash;

        while ((limit = lr.readLine()) >= 0) {
            c = 0;
            keyLen = 0;
            valueStart = limit;
            hasSep = false;

            //System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
            precedingBackslash = false;
            while (keyLen < limit) {
                c = lr.lineBuf[keyLen];
                //need check if escaped.
                if ((c == '=' || c == ':') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    hasSep = true;
                    break;
                } else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    break;
                }
                if (c == '\\') {
                    precedingBackslash = !precedingBackslash;
                } else {
                    precedingBackslash = false;
                }
                keyLen++;
            }
            while (valueStart < limit) {
                c = lr.lineBuf[valueStart];
                if (c != ' ' && c != '\t' && c != '\f') {
                    if (!hasSep && (c == '=' || c == ':')) {
                        hasSep = true;
                    } else {
                        break;
                    }
                }
                valueStart++;
            }
            String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
            String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
            put(key, value);
        }
    }

    private String loadConvert(char[] in, int off, int len, char[] buff) {
        if (buff.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            buff = new char[newLen];
        }
        char[] out = buff;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            char aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    else if (aChar == 'b') aChar = '\b';
                    else if (aChar == '"') aChar = '\"';
                    else if (aChar == '0') aChar = '\0';
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String(out, 0, outLen);
    }

    private String saveConvert(String theString,
                               boolean space,
                               boolean unicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder outBuffer = new StringBuilder(bufLen);
        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || space)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':
                    outBuffer.append("\\t");
                    break;
                case '\n':
                    outBuffer.append("\\n");
                    break;
                case '\r':
                    outBuffer.append("\\r");
                    break;
                case '\f':
                    outBuffer.append("\\f");
                    break;
                case '=':
                case ':':
                case '#':
                case '!':
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & unicode) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    private static void writeComments(BufferedWriter bw, String comments)
            throws IOException {
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;
        char[] uu = new char[6];
        uu[0] = '\\';
        uu[1] = 'u';
        while (current < len) {
            char c = comments.charAt(current);
            if (c > '\u00ff' || c == '\n' || c == '\r') {
                if (last != current)
                    bw.write(comments.substring(last, current));
                if (c > '\u00ff') {
                    uu[2] = toHex((c >> 12) & 0xf);
                    uu[3] = toHex((c >> 8) & 0xf);
                    uu[4] = toHex((c >> 4) & 0xf);
                    uu[5] = toHex(c & 0xf);
                    bw.write(new String(uu));
                } else {
                    bw.newLine();
                    if (c == '\r' &&
                            current != len - 1 &&
                            comments.charAt(current + 1) == '\n') {
                        current++;
                    }
                    if (current == len - 1 ||
                            (comments.charAt(current + 1) != '#' &&
                                    comments.charAt(current + 1) != '!'))
                        bw.write("#");
                }
                last = current + 1;
            }
            current++;
        }
        if (last != current)
            bw.write(comments.substring(last, current));
        bw.newLine();
    }

    public void store(Writer writer, String comments)
            throws IOException {
        store0((writer instanceof BufferedWriter) ? (BufferedWriter) writer
                        : new BufferedWriter(writer),
                comments,
                false);
    }

    public void store(OutputStream out, String comments)
            throws IOException {
        store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")),
                comments,
                true);
    }

    private void store0(BufferedWriter bw, String comments, boolean escUnicode)
            throws IOException {
        if (comments != null) {
            writeComments(bw, comments);
        }
        bw.write("#" + new Date().toString());
        bw.newLine();
        for (Enumeration<?> e = keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            bw.write(saveConvert(key, true, escUnicode) + "=" + saveConvert(get(key), false, escUnicode));
            bw.newLine();
        }
        bw.flush();
    }

    public String getProperty(String key) {
        Object oval = super.get(key);
        return ((oval == null) && (defaults != null)) ? defaults.getProperty(key) : (String) oval;
    }

    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }

    public Set<String> key() {
        Hashtable<String, String> h = new Hashtable<>();
        enumerateStringProperty(h);
        return h.keySet();
    }

    public void list(PrintStream out) {
        out.println("-- listing Property --");
        Hashtable<String, Object> h = new Hashtable<>();
        enumerate(h);
        for (Enumeration<String> e = h.keys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            String val = (String) h.get(key);
            if (val.length() > 40) {
                val = val.substring(0, 37) + "...";
            }
            out.println(key + "=" + val);
        }
    }

    private void enumerate(Hashtable<String, Object> h) {
        if (defaults != null) {
            defaults.enumerate(h);
        }
        for (Enumeration<?> e = keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            h.put(key, get(key));
        }
    }

    private void enumerateStringProperty(Hashtable<String, String> h) {
        if (defaults != null) {
            defaults.enumerateStringProperty(h);
        }
        for (Enumeration<String> e = keys(); e.hasMoreElements(); ) {
            String k = e.nextElement();
            h.put(k, get(k));
        }
    }

    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

}
