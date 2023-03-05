package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.PyList;
import org.python.core.PyObject;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class PyListSerializer extends AbstractPyObjectSerializer<List<?>> {

    private final PyObjectSerializationFactory serializationFactory;

    public PyListSerializer(final PyList list, final PyObjectSerializationFactory serializationFactory) {
        super(list);

        this.serializationFactory = serializationFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<?> serialize() {
        if (!(getValue() instanceof PyList)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Long.",
                    getValue().getClass().getSimpleName()));
        }

        final PyObject[] objects = getValue(PyList.class).getArray();
        final Object[] serializedObjects = new Object[objects.length];

        int index = 0;
        for (final PyObject pyObject : objects) {
            serializedObjects[index] = serializationFactory.getSerializer(pyObject)
                    .serialize();

            index++;
        }

        return Arrays.asList(serializedObjects);
    }

}