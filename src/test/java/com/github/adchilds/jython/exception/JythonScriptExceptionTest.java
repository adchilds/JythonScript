package com.github.adchilds.jython.exception;

import com.github.adchilds.jython.JythonScript;
import com.github.adchilds.jython.exception.JythonScriptException;
import org.junit.Test;

/**
 * Tests for the {@link JythonScriptException} class.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonScriptExceptionTest {

    @Test(expected = JythonScriptException.class)
    public void testJythonScriptException_noFile() throws JythonScriptException {
        JythonScript.compile("");
    }

    @Test(expected = JythonScriptException.class)
    public void testJythonScriptException() throws JythonScriptException {
        throw new JythonScriptException();
    }

    @Test(expected = JythonScriptException.class)
    public void testJythonScriptException_message() throws JythonScriptException {
        throw new JythonScriptException("Test.");
    }

    @Test(expected = JythonScriptException.class)
    public void testJythonScriptException_messageCause() throws JythonScriptException {
        throw new JythonScriptException("Test.", new RuntimeException());
    }

    @Test(expected = JythonScriptException.class)
    public void testJythonScriptException_cause() throws JythonScriptException {
        throw new JythonScriptException(new RuntimeException());
    }

}