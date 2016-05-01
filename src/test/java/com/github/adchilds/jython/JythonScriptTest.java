package com.github.adchilds.jython;

import com.github.adchilds.jython.exception.JythonScriptException;
import com.github.adchilds.jython.JythonScript;
import org.junit.Test;
import org.python.core.PyCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

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

    @Test(expected = JythonScriptException.class)
    public void testCompile_directory() throws JythonScriptException {
        File file = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH).getPath());

        JythonScript.compile(file);
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

    @Test
    public void testExecute_oop() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testOOP.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            JythonScript.execute(compiledScript, 10, 10);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_oop() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testOOP.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript, 10, 10);

            assertNotNull(result);
            assertEquals(51, result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_returnBoolean() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnBoolean.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertTrue(result instanceof Boolean);
            assertEquals(false, result);

            result = JythonScript.evaluate(compiledScript, false, false);
            assertNotNull(result);
            assertTrue(result instanceof Boolean);
            assertEquals(false, result);

            result = JythonScript.evaluate(compiledScript, true, false);
            assertNotNull(result);
            assertTrue(result instanceof Boolean);
            assertEquals(true, result);

            result = JythonScript.evaluate(compiledScript, false, true);
            assertNotNull(result);
            assertTrue(result instanceof Boolean);
            assertEquals(true, result);

            result = JythonScript.evaluate(compiledScript, true, true);
            assertNotNull(result);
            assertTrue(result instanceof Boolean);
            assertEquals(true, result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_returnInteger() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertTrue(result instanceof Integer);
            assertEquals(25, result);

            result = JythonScript.evaluate(compiledScript, 10, 10);
            assertNotNull(result);
            assertTrue(result instanceof Integer);
            assertEquals(100, result);

            result = JythonScript.evaluate(compiledScript, 0, 0);
            assertNotNull(result);
            assertTrue(result instanceof Integer);
            assertEquals(0, result);

            result = JythonScript.evaluate(compiledScript, -1, 20);
            assertNotNull(result);
            assertTrue(result instanceof Integer);
            assertEquals(-20, result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_returnString() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnString.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertTrue(result instanceof String);
            assertEquals("", result);

            result = JythonScript.evaluate(compiledScript, "jython", "test");
            assertNotNull(result);
            assertTrue(result instanceof String);
            assertEquals("jython", result);

            result = JythonScript.evaluate(compiledScript, "test", "jython");
            assertNotNull(result);
            assertTrue(result instanceof String);
            assertEquals("jython", result);

            result = JythonScript.evaluate(compiledScript, "test");
            assertNotNull(result);
            assertTrue(result instanceof String);
            assertEquals("test", result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_returnFloat() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnFloat.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertTrue(result instanceof Float);
            assertEquals(8.778f, result);

            result = JythonScript.evaluate(compiledScript, 10.5, 10.1);
            assertNotNull(result);
            assertTrue(result instanceof Float);
            assertEquals(20.6f, result);

            result = JythonScript.evaluate(compiledScript, 0.0, 0);
            assertNotNull(result);
            assertTrue(result instanceof Float);
            assertEquals(0f, result);

            result = JythonScript.evaluate(compiledScript, -1.5, 20.736587);
            assertNotNull(result);
            assertTrue(result instanceof Float);
            assertEquals(19.236588f, result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_returnLong() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object result = JythonScript.evaluate(compiledScript, 75987958723958L, 3);
            assertNotNull(result);
            assertTrue(result instanceof Long);
            assertEquals(227963876171874L, result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<JythonScript> constructor = JythonScript.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}