package com.github.adchilds.jython.exception;

import com.github.adchilds.jython.JythonScript;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JythonScriptException} class.
 *
 * @author Adam Childs
 * @since 1.0
 */
class JythonScriptExceptionTest {

    private final String EXCEPTION_MESSAGE = "An exception was thrown.";

    @Test
    void testJythonScriptException_invalidFile() {
        assertThrows(JythonScriptException.class, () -> JythonScript.compile(""));
    }

    @Test
    void testJythonScriptException() {
        assertThrows(JythonScriptException.class, () -> {
            throw new JythonScriptException();
        });
    }

    @Test
    void testJythonScriptException_message() {
        final Throwable exception = assertThrows(JythonScriptException.class, () -> {
            throw new JythonScriptException(EXCEPTION_MESSAGE);
        });

        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testJythonScriptException_messageCause() {
        final Throwable exception = assertThrows(JythonScriptException.class, () -> {
            throw new JythonScriptException(EXCEPTION_MESSAGE, new RuntimeException("Source"));
        });

        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

    @Test
    void testJythonScriptException_cause() {
        final Throwable exception = assertThrows(JythonScriptException.class, () -> {
            throw new JythonScriptException(new RuntimeException("Source"));
        });

        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

}