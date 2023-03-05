package com.github.adchilds.jython.exception;

import com.github.adchilds.jython.JythonScript;
import com.github.adchilds.jython.JythonScriptTest;
import com.github.adchilds.jython.serialization.DefaultPyObjectSerializationFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JythonResultNotFoundException} class.
 *
 * @author Adam Childs
 * @since 1.0
 */
class JythonResultNotFoundExceptionTest {

    private final JythonScript jythonScript = new JythonScript.Builder()
            .serializationFactory(new DefaultPyObjectSerializationFactory())
            .build();

    private final String EXCEPTION_MESSAGE = "An exception was thrown.";

    @Test
    void testJythonResultNotFoundException_resultNotFound() {
        final String filePath =
                ClassLoader.getSystemResource(JythonScriptTest.JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();

        assertThrows(JythonResultNotFoundException.class, () -> jythonScript.evaluate(filePath));
    }

    @Test
    void testJythonResultNotFoundException_resultNotFoundMultiArgs() {
        final String filePath =
                ClassLoader.getSystemResource(JythonScriptTest.JYTHON_SCRIPT_BASE_PATH + "testResultNotFound.py").getPath();

        assertThrows(JythonResultNotFoundException.class, () -> jythonScript.evaluate(filePath, 0, 1, 2, 3, 4, 5));
    }

    @Test
    void testJythonResultNotFoundException() {
        assertThrows(JythonResultNotFoundException.class, () -> {
            throw new JythonResultNotFoundException();
        });
    }

    @Test
    void testJythonResultNotFoundException_message() {
        final Throwable exception = assertThrows(JythonResultNotFoundException.class, () -> {
            throw new JythonResultNotFoundException(EXCEPTION_MESSAGE);
        });

        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testJythonResultNotFoundException_messageCause() {
        final Throwable exception = assertThrows(JythonResultNotFoundException.class, () -> {
            throw new JythonResultNotFoundException(EXCEPTION_MESSAGE, new RuntimeException("Source"));
        });

        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

    @Test
    void testJythonResultNotFoundException_cause() {
        final Throwable exception = assertThrows(JythonResultNotFoundException.class, () -> {
            throw new JythonResultNotFoundException(new RuntimeException("Source"));
        });

        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

}