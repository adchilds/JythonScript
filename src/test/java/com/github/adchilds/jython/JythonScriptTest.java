package com.github.adchilds.jython;

import com.github.adchilds.jython.exception.JythonResultNotFoundException;
import com.github.adchilds.jython.exception.JythonScriptException;
import org.junit.Test;
import org.python.core.PyCode;
import org.python.core.PyObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for the {@link JythonScript} class.
 *
 * @author Adam Childs
 * @since 1.0
 */
public class JythonScriptTest {

    public static final String JYTHON_SCRIPT_BASE_PATH = "script/jython/";

    @Test(expected = JythonScriptException.class)
    public void testCompile_filePathNull() throws JythonScriptException {
        JythonScript.compile((String) null);
    }

    @Test
    public void testCompile_filePathEmpty() {
        try {
            JythonScript.compile("");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compile("    ");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compile("\n");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compile("\t");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compile("\r");
        } catch (JythonScriptException e) {
            // This is expected
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testCompile_filePathInvalid() throws JythonScriptException {
        JythonScript.compile("/Users/test/notfound.py");
    }

    @Test
    public void testCompile_filePathValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        try {
            PyCode compiledScript = JythonScript.compile(filePath);
            assertNotNull(compiledScript);
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testCompile_urlNull() throws JythonScriptException {
        JythonScript.compile((URL) null);
    }

    @Test
    public void testCompile_urlValid() throws JythonScriptException {
        URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py");

        try {
            PyCode compiledScript = JythonScript.compile(scriptUrl);
            assertNotNull(compiledScript);
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testCompile_fileNull() throws JythonScriptException {
        JythonScript.compile((File) null);
    }

    @Test(expected = JythonScriptException.class)
    public void testCompile_fileEmpty() throws JythonScriptException {
        JythonScript.compile(new File("/"));
    }

    @Test(expected = JythonScriptException.class)
    public void testCompile_fileInvalid() throws JythonScriptException {
        JythonScript.compile(new File("/Users/test/notfound.py"));
    }

    @Test
    public void testCompile_fileValid() {
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

    @Test(expected = JythonScriptException.class)
    public void testCompileString_null() throws JythonScriptException {
        JythonScript.compileString(null);
    }

    @Test
    public void testCompileString_empty() throws JythonScriptException {
        try {
            JythonScript.compileString("");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compileString("    ");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compileString("\n");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compileString("\t");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.compileString("\r");
        } catch (JythonScriptException e) {
            // This is expected
        }
    }

    @Test
    public void testCompileString_valid() {
        String script = "from java.lang import System\n" +
                "\n" +
                "\n" +
                "def test():\n" +
                "    \"\"\"\n" +
                "    Prints out a simple message for testin purposes.\n" +
                "    \"\"\"\n" +
                "    System.out.println('This is a Jython test.')\n" +
                "    print 'This is a Python test.'\n" +
                "\n" +
                "\n" +
                "if __name__ == '__main__':\n" +
                "    test()";

        PyCode compiledScript = null;
        try {
            compiledScript = JythonScript.compileString(script);
        } catch (JythonScriptException e) {
            fail("Compiling scripts failed. error=[" + e + "]");
        }

        assertNotNull(compiledScript);
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_filePathNull() throws JythonScriptException {
        JythonScript.evaluate((String) null);
    }

    @Test
    public void testEvaluate_filePathEmpty() {
        try {
            JythonScript.evaluate("");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate("    ");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate("\n");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate("\r");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate("\t");
        } catch (JythonScriptException e) {
            // This is expected
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_filePathInvalid() throws JythonScriptException {
        JythonScript.evaluate("/Users/test/notfound.py");

    }

    @Test
    public void testEvaluate_filePath() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        try {
            Object result = JythonScript.evaluate(filePath);
            assertEquals(25, result);

            result = JythonScript.evaluate(filePath, 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(filePath, 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(filePath, -1, 20);
            assertEquals(-20, result);
        } catch (JythonScriptException e) {
            fail("Script execution failed. error=[" + e + "]");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testExecute_filePathNull() throws JythonScriptException {
        JythonScript.execute((String) null);
    }

    @Test
    public void testExecute_filePathEmpty() {
        try {
            JythonScript.execute("");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute("    ");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute("\n");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute("\r");
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute("\t");
        } catch (JythonScriptException e) {
            // This is expected
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testExecute_filePathInvalid() throws JythonScriptException {
        JythonScript.execute("/Users/test/notfound.py");
    }

    @Test
    public void testExecute_filePathValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        try {
            JythonScript.execute(filePath);
        } catch (JythonScriptException e) {
            fail("Script execution failed.");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_urlNull() throws JythonScriptException {
        JythonScript.evaluate((URL) null);
    }

    @Test
    public void testEvaluate_urlValid() throws JythonScriptException {
        URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py");

        try {
            Object result = JythonScript.evaluate(scriptUrl);
            assertEquals(25, result);

            result = JythonScript.evaluate(scriptUrl, 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(scriptUrl, 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(scriptUrl, -1, 20);
            assertEquals(-20, result);
        } catch (JythonScriptException e) {
            fail("Script execution failed. error=[" + e + "]");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testExecute_urlNull() throws JythonScriptException {
        JythonScript.execute((URL) null);
    }

    @Test
    public void testExecute_urlValid() throws JythonScriptException {
        URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py");

        try {
            JythonScript.execute(scriptUrl);
        } catch (JythonScriptException e) {
            fail("Script execution failed.");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_fileNull() throws JythonScriptException {
        JythonScript.evaluate((File) null);
    }

    @Test
    public void testEvaluate_fileEmpty() {
        try {
            JythonScript.evaluate(new File(""));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate(new File("    "));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate(new File("\n"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate(new File("\r"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.evaluate(new File("\t"));
        } catch (JythonScriptException e) {
            // This is expected
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_fileInvalid() throws JythonScriptException {
        JythonScript.evaluate("/Users/test/notfound.py");
    }

    @Test
    public void testEvaluate_fileValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        File file = new File(filePath);

        try {
            Object result = JythonScript.evaluate(file);
            assertEquals(25, result);

            result = JythonScript.evaluate(file, 10, 10);
            assertEquals(100, result);

            result = JythonScript.evaluate(file, 0, 0);
            assertEquals(0, result);

            result = JythonScript.evaluate(file, -1, 20);
            assertEquals(-20, result);
        } catch (JythonScriptException e) {
            fail("Script execution failed. error=[" + e + "]");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testExecute_fileNull() throws JythonScriptException {
        JythonScript.execute((File) null);
    }

    @Test
    public void testExecute_fileEmpty() {
        try {
            JythonScript.execute(new File(""));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute(new File("    "));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute(new File("\n"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute(new File("\r"));
        } catch (JythonScriptException e) {
            // This is expected
        }

        try {
            JythonScript.execute(new File("\t"));
        } catch (JythonScriptException e) {
            // This is expected
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testExecute_fileInvalid() throws JythonScriptException {
        JythonScript.execute(new File("/Users/test/notfound.py"));
    }

    @Test
    public void testExecute_fileValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        try {
            JythonScript.execute(new File(filePath));
        } catch (JythonScriptException e) {
            fail("Script execution failed.");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_inputStreamNull() throws JythonScriptException {
        JythonScript.evaluate((InputStream) null);
    }

    @Test
    public void testEvaluate_inputStreamEmpty() {
        try {
            JythonScript.execute(new FileInputStream(new File("")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("    ")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("\n")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("\t")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("\r")));
        } catch (Exception e) {
            // This is expected
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluate_inputStreamInvalid() throws Exception {
        JythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py")));
    }

    @Test
    public void testEvaluate_inputStream() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        File file = new File(filePath);

        try {
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

    @Test(expected = JythonScriptException.class)
    public void testExecute_inputStreamNull() throws JythonScriptException {
        JythonScript.execute((InputStream) null);
    }

    @Test
    public void testExecute_inputStreamEmpty() {
        try {
            JythonScript.execute(new FileInputStream(new File("")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("    ")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("\n")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("\r")));
        } catch (Exception e) {
            // This is expected
        }

        try {
            JythonScript.execute(new FileInputStream(new File("\t")));
        } catch (Exception e) {
            // This is expected
        }
    }

    @Test(expected = Exception.class)
    public void testExecute_inputStreamInvalid() throws Exception {
        JythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py")));
    }

    @Test
    public void testExecute_inputStreamValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        try {
            JythonScript.execute(new FileInputStream(new File(filePath)));
        } catch (Exception e) {
            fail("Script execution failed.");
        }
    }

    @Test(expected = JythonScriptException.class)
    public void testEvaluate_pycodeNull() throws JythonScriptException {
        JythonScript.evaluate((PyCode) null);
    }

    @Test
    public void testEvaluate_pycodeNoResult() {
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();
            PyCode compiledScript = JythonScript.compile(filePath);

            JythonScript.evaluate(compiledScript);
        } catch (Exception e) {
            // This is expected
            assertTrue(e instanceof JythonResultNotFoundException);
        }
    }

    @Test
    public void testEvaluate_pycodeValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        try {
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

    @Test(expected = JythonScriptException.class)
    public void testExecute_pycodeNull() throws JythonScriptException {
        JythonScript.execute((PyCode) null);
    }

    @Test
    public void testExecute_pycodeValid() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        try {
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
    public void testEvaluate_returnList() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnList.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Object[] result = (Object[]) JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertEquals(12, result.length);
            assertArrayEquals(new Object[] { "a", "b", "c", 1, 2, 3, "do", "re", "mi", 1.0f, 2.0f, 3.0f }, result);
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEvaluate_returnDict() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnDict.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Map<Object, Object> result = (Map<Object, Object>) JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.get("a").equals(1));
            assertTrue(result.get("b").equals(2));
            assertTrue(result.get("c").equals(3));
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEvaluate_returnSet() {
        String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnSet.py").getPath();
        try {
            PyCode compiledScript = JythonScript.compile(filePath);

            Set<Object> result = (Set<Object>) JythonScript.evaluate(compiledScript);
            assertNotNull(result);
            assertEquals(9, result.size());

            Object[] expected = new Object[] { "a", "b", "c", 1, 2, 3, "do", "re", "mi" };
            for (Object object : expected) {
                assertTrue(result.contains(object));
            }
        } catch (JythonScriptException e) {
            fail("Unexpected error occurred.");
        }
    }

    @Test
    public void testEvaluate_unsupportedReturnType() {
        // Valid
        try {
            String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnUnsupportedType.py").getPath();
            Object result = JythonScript.evaluate(filePath);

            assertNotNull(result);
            assertTrue(result instanceof PyObject);
        } catch (JythonScriptException e) {
            fail("Script execution failed. error=[" + e + "]");
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