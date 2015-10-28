package com.adamchilds.jython.exception;

/**
 * Thrown when the result of an evaluated Jython script cannot be found. Any evaluated Jython script must have a local
 * variable named 'result' in order to correctly pass information back to Java.
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonResultNotFoundException extends JythonScriptException {

    /**
     * {@inheritDoc}
     */
    public JythonResultNotFoundException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public JythonResultNotFoundException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public JythonResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public JythonResultNotFoundException(Throwable cause) {
        super(cause);
    }

}