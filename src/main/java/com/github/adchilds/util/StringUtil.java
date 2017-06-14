package com.github.adchilds.util;

/**
 * Provides basic operations for {@link String}s.
 *
 * @author Adam Childs
 * @since 1.0
 */
public class StringUtil {

    /**
     * Determines if the given {@link String} is null or empty.
     *
     * @param value the String to check
     * @return true if the String is null or empty; false otherwise
     * @since 1.0
     */
    public static boolean isBlank(String value) {
        return value == null || value.length() == 0 || value.trim().length() == 0;
    }

    /**
     * Determines if the given {@link String} is not null or empty.
     *
     * @param value the String to check
     * @return true if the String is not null or empty; false otherwise
     * @since 1.0
     */
    public static boolean isNotBlank(String value) {
        return value != null && value.trim().length() > 0;
    }

    // Don't allow this class to be instantiated
    private StringUtil() { }

}