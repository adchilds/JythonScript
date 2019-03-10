package com.github.adchilds.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link FileUtils} class.
 *
 * @author Adam Childs
 * @since 1.1
 */
class FileUtilsTest {

    private static final String JYTHON_SCRIPT_BASE_PATH = "script/jython/";
    private static final String TEST_RESOURCE_BASE_PATH = "test/";
    private static final String PARTIAL_CONTENTS_OF_FILE = "This is a test text file with";

    @Test
    void testGetFileInputStream_stringNull() {
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream((String) null));
    }

    @Test
    void testGetFileInputStream_stringEmpty() {
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream(""));
    }

    @Test
    void testGetFileInputStream_stringInvalid() {
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream("    "));
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream("/Users/test"));
    }

    @Test
    void testGetFileInputStream_stringValid() throws IOException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final InputStream stream = FileUtils.getFileInputStream(filePath);

        assertNotNull(stream);
    }

    @Test
    void testGetFileInputStream_fileNull() {
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream((File) null));
    }

    @Test
    void testGetFileInputStream_fileInvalid() {
        final String dirPath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH).getPath();
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream(new File(dirPath)));
        assertThrows(IOException.class, () -> FileUtils.getFileInputStream(new File("/Users/test")));
    }

    @Test
    void testGetFileInputStream_fileValid() throws IOException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final InputStream stream = FileUtils.getFileInputStream(new File(filePath));

        assertNotNull(stream);
    }

    @Test
    void testReadFully_fileNull() throws IOException {
        assertEquals("", FileUtils.readFully(null));
    }

    @Test
    void testReadFully_fileEmpty() throws IOException {
        final File file = new File(ClassLoader.getSystemResource(TEST_RESOURCE_BASE_PATH + "test_empty.txt").getPath());

        assertEquals("", FileUtils.readFully(file));
    }

    @Test
    void testReadFully_fileInvalid() {
        assertThrows(IOException.class, () -> FileUtils.readFully(new File("/Users/test.py")));
    }

    @Test
    void testReadFully_fileValid() throws IOException {
        final File file = new File(ClassLoader.getSystemResource(TEST_RESOURCE_BASE_PATH + "test_content.txt").getPath());
        final String fileContents = FileUtils.readFully(file);

        assertNotNull(fileContents);
        assertTrue(fileContents.contains(PARTIAL_CONTENTS_OF_FILE));
    }

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<FileUtils> constructor = FileUtils.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}