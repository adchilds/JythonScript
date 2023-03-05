package com.github.adchilds.jython.exception;

/**
 * Extension of a {@link RuntimeException} thrown in potentially unknown circumstances during Jython script execution or
 * evaluation.
 *
 * @author Adam Childs
 * @since 3.0.0
 */
public class JythonScriptRuntimeException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public JythonScriptRuntimeException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptRuntimeException(final String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptRuntimeException(final Throwable cause) {
        super(cause);
    }

}