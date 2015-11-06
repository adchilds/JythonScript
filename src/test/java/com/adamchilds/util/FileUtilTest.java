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

}