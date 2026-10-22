package com.weddingplanner.common.util;

import java.util.ArrayList;
import java.util.List;

/** Escapes values so that '|' or new lines typed by users cannot break the text file. */
public final class FileUtil {

    public static final char DELIMITER = '|';

    private FileUtil() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '|':  sb.append("\\p");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String unescape(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char next = value.charAt(++i);
                switch (next) {
                    case 'p':  sb.append('|');  break;
                    case 'n':  sb.append('\n'); break;
                    default:   sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Join plain values into one escaped line. */
    public static String join(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(DELIMITER);
            }
            sb.append(escape(fields[i]));
        }
        return sb.toString();
    }

    /** Split one line back into plain (unescaped) values. */
    public static String[] split(String line) {
        List<String> parts = new ArrayList<>();
        int start = 0;
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == DELIMITER) {
                parts.add(unescape(line.substring(start, i)));
                start = i + 1;
            }
        }
        return parts.toArray(new String[0]);
    }
}
