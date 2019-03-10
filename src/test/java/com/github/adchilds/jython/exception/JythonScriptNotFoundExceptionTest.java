package com.github.adchilds.jython.exception;

import com.github.adchilds.jython.JythonScript;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JythonScriptNotFoundException} class.
 *
 * @author Adam Childs
 * @since 1.0
 */
class JythonScriptNotFoundExceptionTest {

    private final String EXCEPTION_MESSAGE = "An exception was thrown.";

    @Test
    void testJythonScriptNotFoundException_invalidFile() {
        assertThrows(JythonScriptNotFoundException.class, () -> JythonScript.evaluate("invalidFile.py"));
    }

    @Test
    void testJythonScriptNotFoundException() {
        assertThrows(JythonScriptNotFoundException.class, () -> {
            throw new JythonScriptNotFoundException();
        });
    }

    @Test
    void testJythonScriptNotFoundException_message() {
        final Throwable exception = assertThrows(JythonScriptNotFoundException.class, () -> {
            throw new JythonScriptNotFoundException(EXCEPTION_MESSAGE);
        });

        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testJythonScriptNotFoundException_messageCause() {
        final Throwable exception = assertThrows(JythonScriptNotFoundException.class, () -> {
            throw new JythonScriptNotFoundException(EXCEPTION_MESSAGE, new RuntimeException("Source"));
        });

        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

    @Test
    void testJythonScriptNotFoundException_cause() {
        final Throwable exception = assertThrows(JythonScriptNotFoundException.class, () -> {
            throw new JythonScriptNotFoundException(new RuntimeException("Source"));
        });

        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

}