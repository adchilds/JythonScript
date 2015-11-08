package com.adamchilds.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tests for the {@link FileUtil} class.
 *
 * @author Adam Childs
 * @since 0.2
 */
public class FileUtilTest {

    private static final String JYTHON_SCRIPT_BASE_PATH = "script/jython/";
    private static final String TEST_RESOURCE_BASE_PATH = "test/";
    private static final String CONTENTS_OF_FILE_WITH_CONTENT_WIN = "This is a test text file with\r\n\r\n\r\nmultiple\r\n\r\nlines.";
    private static final String CONTENTS_OF_FILE_WITH_CONTENT_UNIX = "This is a test text file with\n\n\nmultiple\n\nlines.";

    @Test
    public void testGetFileInputStream_string() {
        // Null
        try {
            FileUtil.getFileInputStream((String) null);
        } catch (IOException e) {
            // This is expected
        }

        // Empty
        try {
            FileUtil.getFileInputStream("");
        } catch (IOException e) {
            // This is expected
        }

        // Invalid
        try {
            FileUtil.getFileInputStream("    ");
        } catch (IOException e) {
            // This is expected
        }

        try {
            FileUtil.getFileInputStream("/Users/test");
        } catch (IOException e) {
            // This is expected
        }

        // Valid
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        try {
            FileUtil.getFileInputStream(filePath);
        } catch (IOException e) {
            // This is NOT expected
            fail("Unexpected IOException while converting String to FileInputStream.");
        }
    }

    @Test
    public void testGetFileInputStream_file() {
        // Null
        try {
            FileUtil.getFileInputStream((File) null);
        } catch (IOException e) {
            // This is expected
        }

        // Invalid
        String dirPath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH).getPath();
        try {
            FileUtil.getFileInputStream(new File(dirPath));
        } catch (IOException e) {
            // This is expected
        }

        try {
            FileUtil.getFileInputStream(new File("/Users/test"));
        } catch (IOException e) {
            // This is expected
        }

        // Valid
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        try {
            FileUtil.getFileInputStream(new File(filePath));
        } catch (IOException e) {
            // This is NOT expected
            fail("Unexpected IOException while converting String to FileInputStream.");
        }
    }

    @Test
    public void testReadFully_file() {
        // Null
        try {
            assertEquals("", FileUtil.readFully(null));
        } catch (IOException e) {
            fail("Unexpected error occurred.");
        }

        // Empty
        File file = new File(ClassLoader.getSystemResource(TEST_RESOURCE_BASE_PATH + "test_empty.txt").getPath());
        try {
            assertEquals("", FileUtil.readFully(file));
        } catch (IOException e) {
            fail("Unexpected error occurred.");
        }

        // Invalid
        try {
            FileUtil.readFully(new File("/Users/test.py"));
        } catch (IOException e) {
            // This is expected
        }

        // Valid
        file = new File(ClassLoader.getSystemResource(TEST_RESOURCE_BASE_PATH + "test_content.txt").getPath());
        try {
            String fileContents = FileUtil.readFully(file);

            assertNotNull(fileContents);
            assertTrue(CONTENTS_OF_FILE_WITH_CONTENT_WIN.equals(fileContents) ||
                    CONTENTS_OF_FILE_WITH_CONTENT_UNIX.equals(fileContents));
        } catch (IOException e) {
            fail("Unexpected error occurred.");
        }
    }

}