package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.junit.jupiter.api.Test;
import org.python.core.PyBoolean;
import org.python.core.PyObject;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DefaultPyObjectSerializationFactory} class.
 *
 * @author Adam Childs
 * @since 3.0.0
 */
class DefaultPyObjectSerializationFactoryTest {

    private final PyObjectSerializationFactory factory = new DefaultPyObjectSerializationFactory();

    @Test
    void testGetSerializer_nullObject() {
        assertNull(factory.getSerializer(null));
    }

    @Test
    void testGetSerializer_unsupportedType() {
        final Exception exception = assertThrows(JythonScriptRuntimeException.class,
                () -> factory.getSerializer(new UnsupportedType()));

        assertEquals("Serialization not supported for type [UnsupportedType].", exception.getMessage());
    }

    @Test
    void testGetSerializer_supportedType() {
        final Object result = factory.getSerializer(new PyBoolean(true))
                .serialize();
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     *
     */
    private static class UnsupportedType extends PyObject {

    }

}