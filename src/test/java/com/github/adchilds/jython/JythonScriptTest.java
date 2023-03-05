package com.github.adchilds.jython;

import com.github.adchilds.jython.exception.JythonResultNotFoundException;
import com.github.adchilds.jython.exception.JythonScriptException;
import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import com.github.adchilds.jython.serialization.DefaultPyObjectSerializationFactory;
import com.github.adchilds.jython.serialization.PyObjectSerializationFactory;
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
import java.util.List;
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
    
    private final JythonScript jythonScript = new JythonScript.Builder()
            .serializationFactory(new DefaultPyObjectSerializationFactory())
            .build();

    @Test
    void testCompile_filePathNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile((String) null));
    }

    @Test
    void testCompile_filePathEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile(""));
        assertThrows(JythonScriptException.class, () -> jythonScript.compile("    "));
        assertThrows(JythonScriptException.class, () -> jythonScript.compile("\n"));
        assertThrows(JythonScriptException.class, () -> jythonScript.compile("\t"));
        assertThrows(JythonScriptException.class, () -> jythonScript.compile("\r"));
    }

    @Test
    void testCompile_filePathInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile("/Users/test/notfound.py"));
    }

    @Test
    void testCompile_filePathValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        assertNotNull(jythonScript.compile(filePath));
    }

    @Test
    void testCompile_urlNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile((URL) null));
    }

    @Test
    void testCompile_urlInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile(new URL("http://test")));
    }

    @Test
    void testCompile_urlValid() throws JythonScriptException {
        final URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py");

        assertNotNull(jythonScript.compile(scriptUrl));
    }

    @Test
    void testCompile_fileNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile((File) null));
    }

    @Test
    void testCompile_fileEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile(new File("/")));
    }

    @Test
    void testCompile_fileInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compile(new File("/Users/test/notfound.py")));
    }

    @Test
    void testCompile_fileValid() throws JythonScriptException {
        final File file = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath());

        assertNotNull(jythonScript.compile(file));
    }

    @Test
    void testCompile_directory() {
        final File file = new File(ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH).getPath());

        assertThrows(JythonScriptException.class, () -> jythonScript.compile(file));
    }

    @Test
    void testCompileString_null() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString(null));
    }

    @Test
    void testCompileString_empty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString(""));
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString("    "));
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString("\n"));
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString("\t"));
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString("\r"));
    }

    @Test
    void testCompile_invalidPythonCode() {
        assertThrows(JythonScriptException.class, () -> jythonScript.compileString("Invalid Python code..."));
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

        assertNotNull(jythonScript.compileString(script));
    }

    @Test
    void testEvaluate_filePathNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate((String) null));
    }

    @Test
    void testEvaluate_filePathEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(""));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate("    "));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate("\n"));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate("\t"));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate("\r"));
    }

    @Test
    void testEvaluate_filePathInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate("/Users/test/notfound.py"));

    }

    @Test
    void testEvaluate_filePath() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();

        assertEquals(25, jythonScript.evaluate(filePath));
        assertEquals(100, jythonScript.evaluate(filePath, 10, 10));
        assertEquals(0, jythonScript.evaluate(filePath, 0, 0));
        assertEquals(-20, jythonScript.evaluate(filePath, -1, 20));
    }

    @Test
    void testExecute_filePathNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute((String) null));
    }

    @Test
    void testExecute_filePathEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(""));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute("    "));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute("\n"));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute("\t"));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute("\r"));
    }

    @Test
    void testExecute_filePathInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute("/Users/test/notfound.py"));
    }

    @Test
    void testExecute_filePathValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        jythonScript.execute(filePath);
    }

    @Test
    void testEvaluate_urlNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate((URL) null));
    }

    @Test
    void testEvaluate_urlInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new URL("http://test")));
    }

    @Test
    void testEvaluate_urlNotFound() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new URL("file:///test/test")));
    }

    @Test
    void testEvaluate_urlValid() throws JythonScriptException {
        final URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py");

        assertEquals(25, jythonScript.evaluate(scriptUrl));
        assertEquals(100, jythonScript.evaluate(scriptUrl, 10, 10));
        assertEquals(0, jythonScript.evaluate(scriptUrl, 0, 0));
        assertEquals(-20, jythonScript.evaluate(scriptUrl, -1, 20));
    }

    @Test
    void testExecute_urlNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute((URL) null));
    }

    @Test
    void testExecute_urlInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new URL("http://test")));
    }

    @Test
    void testExecute_urlNotFound() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new URL("file:///test/test")));
    }

    @Test
    void testExecute_urlValid() throws JythonScriptException {
        final URL scriptUrl = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py");

        jythonScript.execute(scriptUrl);
    }

    @Test
    void testEvaluate_fileNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate((File) null));
    }

    @Test
    void testEvaluate_fileEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new File("")));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new File("    ")));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new File("\n")));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new File("\t")));
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new File("\r")));
    }

    @Test
    void testEvaluate_fileInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate("/Users/test/notfound.py"));
    }

    @Test
    void testEvaluate_fileValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final File file = new File(filePath);

        assertEquals(25, jythonScript.evaluate(file));
        assertEquals(100, jythonScript.evaluate(file, 10, 10));
        assertEquals(0, jythonScript.evaluate(file, 0, 0));
        assertEquals(-20, jythonScript.evaluate(file, -1, 20));
    }

    @Test
    void testExecute_fileNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute((File) null));
    }

    @Test
    void testExecute_fileEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new File("")));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new File("    ")));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new File("\n")));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new File("\t")));
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new File("\r")));
    }

    @Test
    void testExecute_fileInvalid() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new File("/Users/test/notfound.py")));
    }

    @Test
    void testExecute_fileValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        jythonScript.execute(new File(filePath));
    }

    @Test
    void testEvaluate_inputStreamNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate((InputStream) null));
    }

    @Test
    void testEvaluate_inputStreamEmpty() {
        assertThrows(FileNotFoundException.class, () -> jythonScript.evaluate(new FileInputStream(new File(""))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.evaluate(new FileInputStream(new File("    "))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.evaluate(new FileInputStream(new File("\n"))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.evaluate(new FileInputStream(new File("\t"))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.evaluate(new FileInputStream(new File("\r"))));
    }

    @Test
    void testEvaluate_inputStreamInvalid() {
        assertThrows(FileNotFoundException.class, () ->
                jythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py"))));
    }

    @Test
    void testEvaluate_inputStream() throws Exception {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final File file = new File(filePath);

        assertEquals(25, jythonScript.evaluate(new FileInputStream(file)));
        assertEquals(100, jythonScript.evaluate(new FileInputStream(file), 10, 10));
        assertEquals(0, jythonScript.evaluate(new FileInputStream(file), 0, 0));
        assertEquals(-20, jythonScript.evaluate(new FileInputStream(file), -1, 20));
    }

    @Test
    void testExecute_inputStreamNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute((InputStream) null));
    }

    @Test
    void testExecute_inputStreamEmpty() {
        assertThrows(FileNotFoundException.class, () -> jythonScript.execute(new FileInputStream(new File(""))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.execute(new FileInputStream(new File("    "))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.execute(new FileInputStream(new File("\n"))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.execute(new FileInputStream(new File("\t"))));
        assertThrows(FileNotFoundException.class, () -> jythonScript.execute(new FileInputStream(new File("\r"))));
    }

    @Test
    void testExecute_inputStreamInvalid() {
        assertThrows(FileNotFoundException.class, () ->
                jythonScript.execute(new FileInputStream(new File("/Users/test/notfound.py"))));
    }

    @Test
    void testExecute_inputStreamValid() throws Exception {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testExecute.py").getPath();

        jythonScript.execute(new FileInputStream(new File(filePath)));
    }

    @Test
    void testEvaluate_pycodeNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate((PyCode) null));
    }

    @Test
    void testEvaluate_pycodeEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.evaluate(new TestPyCode()));
    }

    @Test
    void testEvaluate_pycodeNoResult() {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();

        assertThrows(JythonResultNotFoundException.class, () -> {
            final PyCode compiledScript = jythonScript.compile(filePath);

            jythonScript.evaluate(compiledScript);
        });
    }

    @Test
    void testEvaluate_pycodeValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals(25, jythonScript.evaluate(compiledScript));
        assertEquals(100, jythonScript.evaluate(compiledScript, 10, 10));
        assertEquals(0, jythonScript.evaluate(compiledScript, 0, 0));
        assertEquals(-20, jythonScript.evaluate(compiledScript, -1, 20));
    }

    @Test
    void testExecute_pycodeNull() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute((PyCode) null));
    }

    @Test
    void testExecute_pycodeEmpty() {
        assertThrows(JythonScriptException.class, () -> jythonScript.execute(new TestPyCode()));
    }

    @Test
    void testExecute_pycodeValid() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        jythonScript.execute(compiledScript);
    }

    @Test
    void testExecute_oop() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testOOP.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        jythonScript.execute(compiledScript, 10, 10);
    }

    @Test
    void testEvaluate_oop() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testOOP.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals(51, jythonScript.evaluate(compiledScript, 10, 10));
    }

    @Test
    void testEvaluate_returnBoolean() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnBoolean.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals(false, jythonScript.evaluate(compiledScript));
        assertEquals(false, jythonScript.evaluate(compiledScript, false, false));
        assertEquals(true, jythonScript.evaluate(compiledScript, true, false));
        assertEquals(true, jythonScript.evaluate(compiledScript, false, true));
        assertEquals(true, jythonScript.evaluate(compiledScript, true, true));
    }

    @Test
    void testEvaluate_returnInteger() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals(25, jythonScript.evaluate(compiledScript));
        assertEquals(100, jythonScript.evaluate(compiledScript, 10, 10));
        assertEquals(0, jythonScript.evaluate(compiledScript, 0, 0));
        assertEquals(-20, jythonScript.evaluate(compiledScript, -1, 20));
    }

    @Test
    void testEvaluate_returnString() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnString.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals("", jythonScript.evaluate(compiledScript));
        assertEquals("jython", jythonScript.evaluate(compiledScript, "jython", "test"));
        assertEquals("jython", jythonScript.evaluate(compiledScript, "test", "jython"));
        assertEquals("test", jythonScript.evaluate(compiledScript, "test"));
    }

    @Test
    void testEvaluate_returnFloat() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnFloat.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals(20.6f, (Float) jythonScript.evaluate(compiledScript, 10.5, 10.1), DELTA);
        assertEquals(0f, (Float) jythonScript.evaluate(compiledScript, 0.0, 0), DELTA);
        assertEquals(19.236588f, (Float) jythonScript.evaluate(compiledScript, -1.5, 20.736587), DELTA);
    }

    @Test
    void testEvaluate_returnLong() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertEquals(227963876171874L, jythonScript.evaluate(compiledScript, 75987958723958L, 3));
    }

    @Test
    void testEvaluate_returnList() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnList.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        assertArrayEquals(new Object[] { "a", "b", "c", 1, 2, 3, "do", "re", "mi", 1.0f, 2.0f, 3.0f },
                ((List<?>) jythonScript.evaluate(compiledScript)).toArray());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEvaluate_returnDict() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testReturnDict.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);
        final Map<Object, Object> result = (Map<Object, Object>) jythonScript.evaluate(compiledScript);

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
        final PyCode compiledScript = jythonScript.compile(filePath);
        final Set<Object> result = (Set<Object>) jythonScript.evaluate(compiledScript);

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

        final Exception exception = assertThrows(JythonScriptRuntimeException.class,
                () -> jythonScript.evaluate(filePath));
        assertEquals("Serialization not supported for type [PyInstance].", exception.getMessage());
    }

    @Test
    void testEvaluate_typeNotAsExpected() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        final Exception exception = assertThrows(JythonScriptException.class,
                () -> jythonScript.evaluate(TestReturnType.class, compiledScript));
        assertEquals("Result of type [Integer] cannot be assigned to expected type [com.github.adchilds." +
                "jython.JythonScriptTest$TestReturnType]", exception.getMessage());
    }

    @Test
    void testEvaluate_typeAsExpected() throws JythonScriptException {
        final String filePath = ClassLoader.getSystemResource(JYTHON_SCRIPT_BASE_PATH + "testEvaluate.py").getPath();
        final PyCode compiledScript = jythonScript.compile(filePath);

        final Integer result = jythonScript.evaluate(Integer.class, compiledScript);
        assertNotNull(result);
        assertEquals(25, result);
    }

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException {
        final Constructor<JythonScript> constructor =
                JythonScript.class.getDeclaredConstructor(PyObjectSerializationFactory.class);

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    /**
     * Test implementation of a {@link PyCode} instance. This specific instance is used to cause errors to be thrown
     * by the Jython exec function.
     *
     * @author Adam Childs
     * @since 2.0
     */
    private static class TestPyCode extends PyBaseCode {

        private TestPyCode() {
            co_freevars = new String[]{ "test", "throws exception", "when this array has contents" };
        }

        @Override
        protected PyObject interpret(PyFrame pyFrame, ThreadState threadState) {
            return null;
        }

    }

    /**
     * Example test class for deserializing a Jython script custom return type.
     *
     * @author Adam Childs
     * @since 3.0
     */
    private static class TestReturnType {

        private final String foo;
        private final Integer bar;

        public TestReturnType(final String foo, final Integer bar) {
            this.foo = foo;
            this.bar = bar;
        }

        public String getFoo() {
            return foo;
        }

        public Integer getBar() {
            return bar;
        }

    }

}