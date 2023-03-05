package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.junit.jupiter.api.Test;
import org.python.core.PyBoolean;
import org.python.core.PyString;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PyBooleanSerializer} class.
 *
 * @author Adam Childs
 * @since 3.0.0
 */
class PyBooleanSerializerTest {

    @Test
    void testSerialize_nullValue() {
        final Serializer<Boolean> serializer = new PyBooleanSerializer(null);

        assertNull(serializer.serialize());
    }

    @Test
    void testSerialize_invalidType() {
        final Serializer<Boolean> serializer = new PyBooleanSerializer(new PyString("test"));

        final Exception exception = assertThrows(JythonScriptRuntimeException.class,
                () -> serializer.serialize());
        assertEquals("Cannot serialize type [PyString] as Boolean.", exception.getMessage());
    }

    @Test
    void testSerialize_false() {
        final Serializer<Boolean> serializer = new PyBooleanSerializer(new PyBoolean(false));

        assertFalse(serializer.serialize());
    }

    @Test
    void testSerialize_true() {
        final Serializer<Boolean> serializer = new PyBooleanSerializer(new PyBoolean(true));
        assertTrue(serializer.serialize());
    }

}