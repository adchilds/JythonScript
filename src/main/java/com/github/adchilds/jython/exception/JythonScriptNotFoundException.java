package com.github.adchilds.jython.exception;

import java.io.File;
import java.io.InputStream;

/**
 * Thrown when a given Jython script (represented as a {@link String}, {@link File}, or {@link InputStream}) cannot be
 * located on the host system.
 *
 * @author Adam Childs
 * @since 1.0
 */
public class JythonScriptNotFoundException extends JythonScriptException {

    /**
     * {@inheritDoc}
     */
    public JythonScriptNotFoundException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptNotFoundException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public JythonScriptNotFoundException(Throwable cause) {
        super(cause);
    }

}