package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.PyDictionary;
import org.python.core.PyObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PyDictionarySerializer extends AbstractPyObjectSerializer<Map<Object, Object>> {

    private final PyObjectSerializationFactory serializationFactory;

    public PyDictionarySerializer(final PyDictionary dictionary,
                                  final PyObjectSerializationFactory serializationFactory) {
        super(dictionary);

        this.serializationFactory = serializationFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Object, Object> serialize() {
        if (!(getValue() instanceof PyDictionary)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Map.",
                    getValue().getClass().getSimpleName()));
        }

        final Map<Object, Object> objects = new HashMap<>();

        final Map<PyObject, PyObject> pyObjectMap = getValue(PyDictionary.class).getMap();
        for (final Map.Entry<PyObject, PyObject> entry : pyObjectMap.entrySet()) {
            final Object key = serializationFactory.getSerializer(entry.getKey())
                    .serialize();

            final Object value = serializationFactory.getSerializer(entry.getValue())
                            .serialize();

            objects.put(key, value);
        }

        return objects;
    }

}