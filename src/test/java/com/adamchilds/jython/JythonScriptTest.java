package com.adamchilds.jython;

import com.adamchilds.jython.exception.JythonScriptException;
import org.junit.Test;
import org.python.core.PyCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for the {@link JythonScript} class.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonScriptTest {

    private static final String JYTHON_SCRIPT_BASE_PATH = "script/jython/";

    @Test
    public void testCompile_filePath() {
        // Null
        try {
            JythonScript.compile((String) null);
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.compile("");
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.compile("/Users/test/notfound.py");
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Valid
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        try {
            PyCode compiledScript = JythonScript.compile(filePath);
            assertNotNull(compiledScript);
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }
    }

    @Test
    public void testCompile_filePaths() {
        // Null
        try {
            JythonScript.compile((String) null, (String) null);
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.compile("", "");
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.compile("/Users/test/notfound.py", "/Users/test/test.py");
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Valid
        String filePath1 = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        String filePath2 = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        try {
            Map<String, PyCode> compiledScripts = JythonScript.compile(filePath1, filePath2);
            assertNotNull(compiledScripts);
            assertTrue(compiledScripts.containsKey(filePath1));
            assertTrue(compiledScripts.containsKey(filePath2));
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }
    }

    @Test
    public void testCompile_file() {
        // Null
        try {
            JythonScript.compile((File) null);
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.compile(new File("/"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.compile(new File("/Users/test/notfound.py"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Valid
        File file = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath());

        try {
            PyCode compiledScript = JythonScript.compile(file);
            assertNotNull(compiledScript);
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }
    }

    @Test
    public void testCompile_files() {
        // Null
        try {
            JythonScript.compile((File) null, (File) null);
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.compile(new File("/"), new File("/"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.compile(new File("/Users/test/notfound.py"), new File("/Users/test/test.py"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        // Valid
        File file1 = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath());
        File file2 = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath());

        try {
            Map<String, PyCode> compiledScripts = JythonScript.compile(file1, file2);
            assertNotNull(compiledScripts);
            assertTrue(compiledScripts.containsKey(file1.getAbsolutePath()));
            assertTrue(compiledScripts.containsKey(file2.getAbsolutePath()));
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }
    }

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
    public void testEvaluate_file() {
        // Null
        try {
            JythonScript.evaluate((File) null);
        } catch (Exception e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.evaluate(new File(""));
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
            File file = new File(filePath);

            Object result = JythonScript.evaluate(file);
            assertEquals(25, result);

            result = JythonScript.evaluate(file, 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(file, 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(file, -1, 20);
            assertEquals(-20, result);
        } catch (Exception e) {
            fail("Script execution failed. error=[" + e + "]");
        }
    }

    @Test
    public void testExecute_file() {
        // Null
        try {
            JythonScript.execute((File) null);
        } catch (Exception e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.execute(new File(""));
        } catch (Exception e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.execute(new File("/Users/test/notfound.py"));
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

            JythonScript.execute(new File(filePath));
        } catch (Exception e) {
            fail("Script execution failed.");
        }
    }

    @Test
    public void testEvaluate_inputStream() {
        // Null
        try {
            JythonScript.evaluate((InputStream) null);
        } catch (Exception e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.execute(new FileInputStream(new File("")));
        } catch (Exception e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py")));
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
            File file = new File(filePath);

            Object result = JythonScript.evaluate(new FileInputStream(file));
            assertEquals(25, result);

            result = JythonScript.evaluate(new FileInputStream(file), 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(new FileInputStream(file), 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(new FileInputStream(file), -1, 20);
            assertEquals(-20, result);
        } catch (Exception e) {
            fail("Script execution failed. error=[" + e + "]");
        }
    }

    @Test
    public void testExecute_inputStream() {
        // Null
        try {
            JythonScript.execute((InputStream) null);
        } catch (Exception e) {
            // This is expected
        }

        // Empty
        try {
            JythonScript.execute(new FileInputStream(new File("")));
        } catch (Exception e) {
            // This is expected
        }

        // Invalid
        try {
            JythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py")));
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

            JythonScript.execute(new FileInputStream(new File(filePath)));
        } catch (Exception e) {
            fail("Script execution failed.");
        }
    }

    @Test
    public void testEvaluate_pycode() {
        // Null
        try {
            Object result = JythonScript.evaluate((PyCode) null);
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript);
            assertEquals(25, result);

            result = JythonScript.evaluate(compiledScript, 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(compiledScript, 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(compiledScript, -1, 20);
            assertEquals(-20, result);
        } catch (Exception e) {
            fail("Script execution failed.");
        }
    }

    @Test
    public void testExecute_pycode() {
        // Null
        try {
            JythonScript.execute((PyCode) null);
        } catch (Exception e) {
            // This is expected
        }

        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
            PyCode compiledScript = JythonScript.compile(filePath);

            JythonScript.execute(compiledScript);
        } catch (Exception e) {
            fail("Script execution failed.");
        }
    }

}