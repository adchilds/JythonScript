package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.Py;
import org.python.core.PyLong;
import org.python.core.PyObject;

/**
 *
 */
public class PyLongSerializer extends AbstractPyObjectSerializer<Long> {

    protected PyLongSerializer(final PyObject value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long serialize() {
        if (!(getValue() instanceof PyLong)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Long.",
                    getValue().getClass().getSimpleName()));
        }

        return Py.py2long(getValue());
    }

}