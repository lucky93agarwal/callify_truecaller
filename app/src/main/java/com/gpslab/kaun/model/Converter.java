package com.gpslab.kaun.model;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Converter {

    /**
     * Private constructor.
     */
    private Converter() {
    }

    /**
     * Pattern for NCR.
     */
    private static final Pattern PATTERN = Pattern.compile("&#([0-9]{1,7});");

    /**
     * Converts a string containing &#...; escapes to a string of characters.
     *
     * @param str {@link CharSequence} to convert
     * @return converted {@link CharSequence}
     */
    public static CharSequence convertDecNCR2Char(final CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        Matcher m = PATTERN.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String c = m.group();
            m.appendReplacement(sb, dec2char(c.substring(2, c.length() - 1)));

        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * Converts a single string representing a decimal number to a character note that no checking
     * is performed to ensure that this is just a hex number, eg. no spaces etc dec: string, the dec
     * codepoint to be converted.
     *
     * @param str single {@link String}
     * @return decoded {@link String}
     */
    private static String dec2char(final String str) {
        try {
            int n = Integer.valueOf(str);
            if (n <= 0xFFFF) {
                return String.valueOf((char) n);
            } else if (n <= 0x10FFFF) {
                n -= 0x10000;
                return String.valueOf((char) (0xD800 | (n >> 10)))
                        + String.valueOf((char) (0xDC00 | (n & 0x3FF)));
            }
        } catch (NumberFormatException nfe) {
            return str;
        }
        return str;
    }
}
