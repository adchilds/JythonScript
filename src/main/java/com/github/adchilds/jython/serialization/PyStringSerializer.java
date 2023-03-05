package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.PyObject;
import org.python.core.PyString;

/**
 *
 */
public class PyStringSerializer extends AbstractPyObjectSerializer<String> {

    protected PyStringSerializer(final PyObject value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialize() {
        if (!(getValue() instanceof PyString)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as String.",
                    getValue().getClass().getSimpleName()));
        }

        return getValue(PyString.class).getString();
    }

}