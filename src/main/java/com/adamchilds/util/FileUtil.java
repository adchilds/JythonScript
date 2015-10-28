package com.adamchilds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Provides static file operations, such as converting a {@link String} or {@link File} to an {@link InputStream}.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class FileUtil {

    /**
     * Attempts to convert the given {@code object} to an {@link InputStream}. If the object cannot be converted, throws
     * an {@link Exception}.
     *
     * Current supported object conversions include:
     * <ul>
     *     <li>{@link String} - as a fully qualified file path</li>
     *     <li>{@link File}</li>
     * </ul>
     *
     * @param object the object to convert to an input stream
     * @return a new {@link InputStream} created from the given {@code object}
     */
    public static InputStream getFileInputStream(Object object) throws Exception {
        if (object instanceof String) {
            return new FileInputStream((String) object);
        } else if (object instanceof File) {
            return new FileInputStream((File) object);
        }

        throw new Exception("Could not convert the given object to an InputStream. object=[" + object.toString() + "]");
    }

}