package com.github.adchilds.jython.exception;

import com.github.adchilds.jython.JythonScript;
import com.github.adchilds.jython.JythonScriptTest;
import org.junit.Test;

/**
 * Tests for the {@link JythonResultNotFoundException} class.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonResultNotFoundExceptionTest {

    @Test(expected = JythonResultNotFoundException.class)
    public void testJythonResultNotFoundException_resultNotFound() throws JythonScriptException {
        String filePath = ClassLoader.getSystemResource(JythonScriptTest.JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();

        JythonScript.evaluate(filePath);
    }

    @Test(expected = JythonResultNotFoundException.class)
    public void testJythonResultNotFoundException_resultNotFoundMultiArgs() throws JythonScriptException {
        String filePath = ClassLoader.getSystemResource(JythonScriptTest.JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();

        JythonScript.evaluate(filePath, 0, 1, 2, 3, 4, 5);
    }

    @Test(expected = JythonResultNotFoundException.class)
    public void testJythonResultNotFoundException() throws JythonScriptException {
        throw new JythonResultNotFoundException();
    }

    @Test(expected = JythonResultNotFoundException.class)
    public void testJythonResultNotFoundException_message() throws JythonScriptException {
        throw new JythonResultNotFoundException("Test.");
    }

    @Test(expected = JythonResultNotFoundException.class)
    public void testJythonResultNotFoundException_messageCause() throws JythonScriptException {
        throw new JythonResultNotFoundException("Test.", new RuntimeException());
    }

    @Test(expected = JythonResultNotFoundException.class)
    public void testJythonResultNotFoundException_cause() throws JythonScriptException {
        throw new JythonResultNotFoundException(new RuntimeException());
    }

}