package com.github.adchilds.jython.exception;

import com.github.adchilds.jython.JythonScript;
import org.junit.Test;

/**
 * Tests for the {@link JythonScriptNotFoundException} class.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonScriptNotFoundExceptionTest {

    @Test(expected = JythonScriptNotFoundException.class)
    public void testJythonScriptNotFoundException_invalidFile() throws JythonScriptException {
        JythonScript.evaluate("invalidFile.py");
    }

    @Test(expected = JythonScriptNotFoundException.class)
    public void testJythonScriptNotFoundException() throws JythonScriptException {
        throw new JythonScriptNotFoundException();
    }

    @Test(expected = JythonScriptNotFoundException.class)
    public void testJythonScriptNotFoundException_message() throws JythonScriptException {
        throw new JythonScriptNotFoundException("Test.");
    }

    @Test(expected = JythonScriptNotFoundException.class)
    public void testJythonScriptNotFoundException_messageCause() throws JythonScriptException {
        throw new JythonScriptNotFoundException("Test.", new RuntimeException());
    }

    @Test(expected = JythonScriptNotFoundException.class)
    public void testJythonScriptNotFoundException_cause() throws JythonScriptException {
        throw new JythonScriptNotFoundException(new RuntimeException());
    }

}