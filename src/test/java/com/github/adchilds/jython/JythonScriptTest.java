package com.github.adchilds.jython;

import com.github.adchilds.jython.exception.JythonResultNotFoundException;
import com.github.adchilds.jython.exception.JythonScriptException;
import org.junit.jupiter.api.Test;
import org.python.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JythonScript} class.
 *
 * @author Adam Childs
 * @since 1.0
 */
public class JythonScriptTest {

    private static final double DELTA = 1E15;

    public static final String JYTHON_SCRIPT_BASE_PATH = "script/jython/";


    @Test
    void testCompile_filePathNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile((String) null));
    }

    @Test
    void testCompile_filePathEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile(""));
        assertThrows(JythonScriptException.class, () -> JythonScript.compile("    "));
        assertThrows(JythonScriptException.class, () -> JythonScript.compile("\n"));
        assertThrows(JythonScriptException.class, () -> JythonScript.compile("\t"));
        assertThrows(JythonScriptException.class, () -> JythonScript.compile("\r"));
    }

    @Test
    void testCompile_filePathInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile("/Users/test/notfound.py"));
    }

    @Test
    void testCompile_filePathValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        assertNotNull(JythonScript.compile(filePath));
    }

    @Test
    void testCompile_urlNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile((URL) null));
    }

    @Test
    void testCompile_urlInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile(new URL("http://test")));
    }

    @Test
    void testCompile_urlValid() throws JythonScriptException {
        final URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py");

        assertNotNull(JythonScript.compile(scriptUrl));
    }

    @Test
    void testCompile_fileNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile((File) null));
    }

    @Test
    void testCompile_fileEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile(new File("/")));
    }

    @Test
    void testCompile_fileInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile(new File("/Users/test/notfound.py")));
    }

    @Test
    void testCompile_fileValid() throws JythonScriptException {
        final File file = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath());

        assertNotNull(JythonScript.compile(file));
    }

    @Test
    void testCompile_directory() {
        final File file = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH).getPath());

        assertThrows(JythonScriptException.class, () -> JythonScript.compile(file));
    }

    @Test
    void testCompileString_null() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compileString(null));
    }

    @Test
    void testCompileString_empty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compileString(""));
        assertThrows(JythonScriptException.class, () -> JythonScript.compileString("    "));
        assertThrows(JythonScriptException.class, () -> JythonScript.compileString("\n"));
        assertThrows(JythonScriptException.class, () -> JythonScript.compileString("\t"));
        assertThrows(JythonScriptException.class, () -> JythonScript.compileString("\r"));
    }

    @Test
    void testCompileString_valid() throws JythonScriptException {
        final String script =
                "from java.lang import System\n" +
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

        assertNotNull(JythonScript.compileString(script));
    }

    @Test
    void testEvaluate_filePathNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate((String) null));
    }

    @Test
    void testEvaluate_filePathEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(""));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate("    "));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate("\n"));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate("\t"));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate("\r"));
    }

    @Test
    void testEvaluate_filePathInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate("/Users/test/notfound.py"));

    }

    @Test
    void testEvaluate_filePath() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        assertEquals(25, JythonScript.evaluate(filePath));
        assertEquals(100, JythonScript.evaluate(filePath, 10, 10));
        assertEquals(0, JythonScript.evaluate(filePath, 0, 0));
        assertEquals(-20, JythonScript.evaluate(filePath, -1, 20));
    }

    @Test
    void testExecute_filePathNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute((String) null));
    }

    @Test
    void testExecute_filePathEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(""));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute("    "));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute("\n"));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute("\t"));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute("\r"));
    }

    @Test
    void testExecute_filePathInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute("/Users/test/notfound.py"));
    }

    @Test
    void testExecute_filePathValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        JythonScript.execute(filePath);
    }

    @Test
    void testEvaluate_urlNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate((URL) null));
    }

    @Test
    void testEvaluate_urlInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new URL("http://test")));
    }

    @Test
    void testEvaluate_urlNotFound() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new URL("file:///test/test")));
    }

    @Test
    void testEvaluate_urlValid() throws JythonScriptException {
        final URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py");

        assertEquals(25, JythonScript.evaluate(scriptUrl));
        assertEquals(100, JythonScript.evaluate(scriptUrl, 10, 10));
        assertEquals(0, JythonScript.evaluate(scriptUrl, 0, 0));
        assertEquals(-20, JythonScript.evaluate(scriptUrl, -1, 20));
    }

    @Test
    void testExecute_urlNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute((URL) null));
    }

    @Test
    void testExecute_urlInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new URL("http://test")));
    }

    @Test
    void testExecute_urlNotFound() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new URL("file:///test/test")));
    }

    @Test
    void testExecute_urlValid() throws JythonScriptException {
        final URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py");

        JythonScript.execute(scriptUrl);
    }

    @Test
    void testEvaluate_fileNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate((File) null));
    }

    @Test
    void testEvaluate_fileEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new File("")));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new File("    ")));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new File("\n")));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new File("\t")));
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new File("\r")));
    }

    @Test
    void testEvaluate_fileInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate("/Users/test/notfound.py"));
    }

    @Test
    void testEvaluate_fileValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final File file = new File(filePath);

        assertEquals(25, JythonScript.evaluate(file));
        assertEquals(100, JythonScript.evaluate(file, 10, 10));
        assertEquals(0, JythonScript.evaluate(file, 0, 0));
        assertEquals(-20, JythonScript.evaluate(file, -1, 20));
    }

    @Test
    void testExecute_fileNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute((File) null));
    }

    @Test
    void testExecute_fileEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new File("")));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new File("    ")));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new File("\n")));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new File("\t")));
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new File("\r")));
    }

    @Test
    void testExecute_fileInvalid() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new File("/Users/test/notfound.py")));
    }

    @Test
    void testExecute_fileValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        JythonScript.execute(new File(filePath));
    }

    @Test
    void testEvaluate_inputStreamNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate((InputStream) null));
    }

    @Test
    void testEvaluate_inputStreamEmpty() {
        assertThrows(FileNotFoundException.class, () -> JythonScript.evaluate(new FileInputStream(new File(""))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.evaluate(new FileInputStream(new File("    "))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.evaluate(new FileInputStream(new File("\n"))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.evaluate(new FileInputStream(new File("\t"))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.evaluate(new FileInputStream(new File("\r"))));
    }

    @Test
    void testEvaluate_inputStreamInvalid() {
        assertThrows(FileNotFoundException.class, () ->
                JythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py"))));
    }

    @Test
    void testEvaluate_inputStream() throws Exception {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final File file = new File(filePath);

        assertEquals(25, JythonScript.evaluate(new FileInputStream(file)));
        assertEquals(100, JythonScript.evaluate(new FileInputStream(file), 10, 10));
        assertEquals(0, JythonScript.evaluate(new FileInputStream(file), 0, 0));
        assertEquals(-20, JythonScript.evaluate(new FileInputStream(file), -1, 20));
    }

    @Test
    void testExecute_inputStreamNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute((InputStream) null));
    }

    @Test
    void testExecute_inputStreamEmpty() {
        assertThrows(FileNotFoundException.class, () -> JythonScript.execute(new FileInputStream(new File(""))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.execute(new FileInputStream(new File("    "))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.execute(new FileInputStream(new File("\n"))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.execute(new FileInputStream(new File("\t"))));
        assertThrows(FileNotFoundException.class, () -> JythonScript.execute(new FileInputStream(new File("\r"))));
    }

    @Test
    void testExecute_inputStreamInvalid() {
        assertThrows(FileNotFoundException.class, () ->
                JythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py"))));
    }

    @Test
    void testExecute_inputStreamValid() throws Exception {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        JythonScript.execute(new FileInputStream(new File(filePath)));
    }

    @Test
    void testEvaluate_pycodeNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate((PyCode) null));
    }

    @Test
    void testEvaluate_pycodeEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.evaluate(new TestPyCode()));
    }

    @Test
    void testEvaluate_pycodeNoResult() {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();

        assertThrows(JythonResultNotFoundException.class, () -> {
            final PyCode compiledScript = JythonScript.compile(filePath);

            JythonScript.evaluate(compiledScript);
        });
    }

    @Test
    void testEvaluate_pycodeValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals(25, JythonScript.evaluate(compiledScript));
        assertEquals(100, JythonScript.evaluate(compiledScript, 10, 10));
        assertEquals(0, JythonScript.evaluate(compiledScript, 0, 0));
        assertEquals(-20, JythonScript.evaluate(compiledScript, -1, 20));
    }

    @Test
    void testExecute_pycodeNull() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute((PyCode) null));
    }

    @Test
    void testExecute_pycodeEmpty() {
        assertThrows(JythonScriptException.class, () -> JythonScript.execute(new TestPyCode()));
    }

    @Test
    void testExecute_pycodeValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        JythonScript.execute(compiledScript);
    }

    @Test
    void testExecute_oop() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testOOP.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        JythonScript.execute(compiledScript, 10, 10);
    }

    @Test
    void testEvaluate_oop() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testOOP.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals(51, JythonScript.evaluate(compiledScript, 10, 10));
    }

    @Test
    void testEvaluate_returnBoolean() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnBoolean.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals(false, JythonScript.evaluate(compiledScript));
        assertEquals(false, JythonScript.evaluate(compiledScript, false, false));
        assertEquals(true, JythonScript.evaluate(compiledScript, true, false));
        assertEquals(true, JythonScript.evaluate(compiledScript, false, true));
        assertEquals(true, JythonScript.evaluate(compiledScript, true, true));
    }

    @Test
    void testEvaluate_returnInteger() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals(25, JythonScript.evaluate(compiledScript));
        assertEquals(100, JythonScript.evaluate(compiledScript, 10, 10));
        assertEquals(0, JythonScript.evaluate(compiledScript, 0, 0));
        assertEquals(-20, JythonScript.evaluate(compiledScript, -1, 20));
    }

    @Test
    void testEvaluate_returnString() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnString.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals("", JythonScript.evaluate(compiledScript));
        assertEquals("jython", JythonScript.evaluate(compiledScript, "jython", "test"));
        assertEquals("jython", JythonScript.evaluate(compiledScript, "test", "jython"));
        assertEquals("test", JythonScript.evaluate(compiledScript, "test"));
    }

    @Test
    void testEvaluate_returnFloat() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnFloat.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals(20.6f, (Float) JythonScript.evaluate(compiledScript, 10.5, 10.1), DELTA);
        assertEquals(0f, (Float) JythonScript.evaluate(compiledScript, 0.0, 0), DELTA);
        assertEquals(19.236588f, (Float) JythonScript.evaluate(compiledScript, -1.5, 20.736587), DELTA);
    }

    @Test
    void testEvaluate_returnLong() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertEquals(227963876171874L, JythonScript.evaluate(compiledScript, 75987958723958L, 3));
    }

    @Test
    void testEvaluate_returnList() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnList.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);

        assertArrayEquals(new Object[] { "a", "b", "c", 1, 2, 3, "do", "re", "mi", 1.0f, 2.0f, 3.0f },
                (Object[]) JythonScript.evaluate(compiledScript));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEvaluate_returnDict() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnDict.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);
        final Map<Object, Object> result = (Map<Object, Object>) JythonScript.evaluate(compiledScript);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get("a"));
        assertEquals(2, result.get("b"));
        assertEquals(3, result.get("c"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEvaluate_returnSet() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnSet.py").getPath();
        final PyCode compiledScript = JythonScript.compile(filePath);
        final Set<Object> result = (Set<Object>) JythonScript.evaluate(compiledScript);

        assertNotNull(result);
        assertEquals(9, result.size());

        final Object[] expected = new Object[] { "a", "b", "c", 1, 2, 3, "do", "re", "mi" };
        for (final Object object : expected) {
            assertTrue(result.contains(object));
        }
    }

    @Test
    void testEvaluate_unsupportedReturnType() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnUnsupportedType.py").getPath();
        final Object result = JythonScript.evaluate(filePath);

        assertNotNull(result);
        assertTrue(result instanceof PyInstance);
    }

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<JythonScript> constructor = JythonScript.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * Test implementation of a {@link PyCode} instance. This specific instance is used to cause errors to be thrown
     * by the Jython exec function.
     *
     * @author Adam Childs
     * @since 2.0
     */
    private class TestPyCode extends PyBaseCode {

        private TestPyCode() {
            co_freevars = new String[]{ "test", "throws exception", "when this array has contents" };
        }

        @Override
        protected PyObject interpret(PyFrame pyFrame, ThreadState threadState) {
            return null;
        }

    }

}