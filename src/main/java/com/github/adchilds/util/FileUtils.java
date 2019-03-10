package com.github.adchilds.util;

import java.io.*;

/**
 * Provides static file operations, such as converting a {@link String} or {@link File} to an {@link InputStream}.
 *
 * @author Adam Childs
 * @since 1.0
 */
public final class FileUtils {

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
     * @throws IOException when the given Object cannot be converted to a FileInputStream
     * @since 1.0
     */
    public static <T> InputStream getFileInputStream(T object) throws IOException {
        if (object instanceof String) {
            return new FileInputStream((String) object);
        } else if (object instanceof File) {
            return new FileInputStream((File) object);
        }

        throw new IOException("Could not convert the given object to an InputStream. object=[" + object + "]");
    }

    /**
     * Reads the entire contents of the given {@link File} to a {@link String}. Defaults to the UTF-8 character encoding.
     *
     * @param file the {@link File} to read the contents of
     * @return a new {@link String} that contains the contents of the given file
     * @throws IOException when the given File cannot be found or read from
     * @since 1.0
     */
    public static String readFully(File file) throws IOException {
        if (file == null) {
            return "";
        }

        return readFully(new FileInputStream(file), "UTF-8");
    }

    /**
     * Reads the entire contents of the given {@link InputStream} to a {@link String}.
     *
     * @param inputStream the {@link InputStream} to read the contents of
     * @param encoding the character encoding that the resulting {@link String} should be (i.e. UTF-8)
     * @return a new {@link String} that contains the contents of the given stream
     * @throws IOException when the given InputStream cannot be read from
     * @since 1.0
     */
    public static String readFully(InputStream inputStream, String encoding) throws IOException {
        return new String(readFully(inputStream), encoding);
    }

    /**
     * Reads the entire contents of the given {@link InputStream} to a new byte array.
     *
     * @param inputStream the {@link InputStream} to read the contents of
     * @return a new array of bytes that contains the contents of the given stream
     * @throws IOException when the given InputStream cannot be read from
     * @since 1.0
     */
    private static byte[] readFully(InputStream inputStream) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }

        return baos.toByteArray();
    }

    // Don't allow this class to be instantiated
    private FileUtils() { }

}