package com.adamchilds.jython.util;

import com.adamchilds.jython.JythonScript;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the {@link JythonScript} class.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonScriptTest {

    public static final String JYTHON_SCRIPT_BASE_PATH = "script/jython/";

    @Test
    public void testEvaluate_filePath() {
        // Null
        try {
            JythonScript.evaluate((String) null);
        } catch (Exception e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.evaluate("");
        } catch (Exception e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.evaluate("/Users/test/notfound.py");
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

            Object result = JythonScript.evaluate(filePath);
            assertEquals(25, result);

            result = JythonScript.evaluate(filePath, 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(filePath, 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(filePath, -1, 20);
            assertEquals(-20, result);
        } catch (Exception e) {
            fail("Script execution failed. error=[" + e + "]");
        }
    }

    @Test
    public void testExecute_filePath() {
        // Null
        try {
            JythonScript.execute((String) null);
        } catch (Exception e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.execute("");
        } catch (Exception e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.execute("/Users/test/notfound.py");
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

            JythonScript.execute(filePath);
        } catch (Exception e) {
            fail("Script execution failed.");
        }
    }

    @Test
    public void testExecute_file() {

    }

    @Test
    public void testExecute_inputStream() {

    }

}