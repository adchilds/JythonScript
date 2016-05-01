package com.github.adchilds.jython.exception;

/**
 * Generic Jython script execution/evaluation exception thrown in potentially unknown circumstances during Jython script
 * execution or evaluation.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonScriptException extends Exception {

    /**
     * {@inheritDoc}
     */
    public JythonScriptException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptException(Throwable cause) {
        super(cause);
    }

}