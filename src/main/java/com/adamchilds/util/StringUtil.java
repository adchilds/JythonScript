package com.adamchilds.util;

/**
 *
 *
 * @author Adam Childs
 * @since 0.1
 */
public class StringUtil {

    /**
     *
     * @param value
     * @return
     */
    public static boolean isBlank(String value) {
        return value == null || value.length() == 0;
    }

    /**
     *
     * @param value
     * @return
     */
    public static boolean isNotBlank(String value) {
        return value != null && value.length() > 0;
    }

}