package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.junit.jupiter.api.Test;
import org.python.core.PyFloat;
import org.python.core.PyString;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PyFloatSerializer} class.
 *
 * @author Adam Childs
 * @since 3.0.0
 */
class PyFloatSerializerTest {

    private static final double DELTA = 1E-15;

    @Test
    void testSerialize_nullValue() {
        final Serializer<Float> serializer = new PyFloatSerializer(null);

        assertNull(serializer.serialize());
    }

    @Test
    void testSerialize_invalidType() {
        final Serializer<Float> serializer = new PyFloatSerializer(new PyString("test"));

        final Exception exception = assertThrows(JythonScriptRuntimeException.class,
                () -> serializer.serialize());
        assertEquals("Cannot serialize type [PyString] as Float.", exception.getMessage());
    }

    @Test
    void testSerialize_negative() {
        final Serializer<Float> serializer = new PyFloatSerializer(new PyFloat(-1.25));

        assertEquals(-1.25, serializer.serialize(), DELTA);
    }

    @Test
    void testSerialize_positive() {
        final Serializer<Float> serializer = new PyFloatSerializer(new PyFloat(1.25));

        assertEquals(1.25, serializer.serialize(), DELTA);
    }

    @Test
    void testSerialize_floatMax() {
        final Serializer<Float> serializer = new PyFloatSerializer(new PyFloat(Float.MAX_VALUE));

        assertEquals(Float.MAX_VALUE, serializer.serialize(), DELTA);
    }

}