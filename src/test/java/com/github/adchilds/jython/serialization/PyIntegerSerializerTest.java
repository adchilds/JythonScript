package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.junit.jupiter.api.Test;
import org.python.core.PyInteger;
import org.python.core.PyString;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PyIntegerSerializer} class.
 *
 * @author Adam Childs
 * @since 3.0.0
 */
class PyIntegerSerializerTest {

    @Test
    void testSerialize_nullValue() {
        final Serializer<Integer> serializer = new PyIntegerSerializer(null);

        assertNull(serializer.serialize());
    }

    @Test
    void testSerialize_invalidType() {
        final Serializer<Integer> serializer = new PyIntegerSerializer(new PyString("test"));

        final Exception exception = assertThrows(JythonScriptRuntimeException.class,
                () -> serializer.serialize());
        assertEquals("Cannot serialize type [PyString] as Integer.", exception.getMessage());
    }

    @Test
    void testSerialize_negative() {
        final Serializer<Integer> serializer = new PyIntegerSerializer(new PyInteger(-10));

        assertEquals(-10, serializer.serialize());
    }

    @Test
    void testSerialize_positive() {
        final Serializer<Integer> serializer = new PyIntegerSerializer(new PyInteger(10));

        assertEquals(10, serializer.serialize());
    }

    @Test
    void testSerialize_integerMax() {
        final Serializer<Integer> serializer = new PyIntegerSerializer(new PyInteger(Integer.MAX_VALUE));

        assertEquals(Integer.MAX_VALUE, serializer.serialize());
    }

}