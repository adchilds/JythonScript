package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.PyObject;
import org.python.core.PySet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class PySetSerializer extends AbstractPyObjectSerializer<Set<Object>> {

    private final PyObjectSerializationFactory serializationFactory;

    public PySetSerializer(final PySet set, final PyObjectSerializationFactory serializationFactory) {
        super(set);

        this.serializationFactory = serializationFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> serialize() {
        if (!(getValue() instanceof PySet)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Set.",
                    getValue().getClass().getSimpleName()));
        }

        final Set<Object> objects = new HashSet<>();

        final Set<PyObject> pySet = getValue(PySet.class).getSet();
        for (final PyObject pyObject : pySet) {
            Optional.ofNullable(serializationFactory.getSerializer(pyObject))
                    .map(Serializer::serialize)
                    .ifPresent(objects::add);
        }

        return objects;
    }

}