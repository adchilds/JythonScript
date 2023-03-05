package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.*;

/**
 * Implementation of a {@link PyObjectSerializationFactory} that provides serialization capabilities for several
 * extensions of the {@link PyObject} type.
 *
 * @author Adam Childs
 * @since 3.0.0
 */
public class DefaultPyObjectSerializationFactory implements PyObjectSerializationFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends PyObject> Serializer<?> getSerializer(final T object) {
        if (object == null) {
            // We should never get here since evaluate provides this check; but, just in case.
            return null;
        }

        if (object instanceof PyBoolean) {
            return new PyBooleanSerializer(object);
        } else if (object instanceof PyInteger) {
            return new PyIntegerSerializer(object);
        } else if (object instanceof PyString) {
            return new PyStringSerializer(object);
        } else if (object instanceof PyFloat) {
            return new PyFloatSerializer(object);
        } else if (object instanceof PyLong) {
            return new PyLongSerializer(object);
        } else if (object instanceof PyList) {
            return new PyListSerializer((PyList) object, this);
        } else if (object instanceof PyDictionary) {
            return new PyDictionarySerializer((PyDictionary) object, this);
        } else if (object instanceof PySet) {
            return new PySetSerializer((PySet) object, this);
        }

        throw new JythonScriptRuntimeException(String.format("Serialization not supported for type [%s].",
                object.getClass().getSimpleName()));
    }

}