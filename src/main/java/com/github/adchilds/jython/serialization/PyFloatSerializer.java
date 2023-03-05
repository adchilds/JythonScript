package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.Py;
import org.python.core.PyFloat;
import org.python.core.PyObject;

/**
 *
 */
public class PyFloatSerializer extends AbstractPyObjectSerializer<Float> {

    protected PyFloatSerializer(final PyObject value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float serialize() {
        if (getValue() == null) {
            return null;
        }

        if (!(getValue() instanceof PyFloat)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Float.",
                    getValue().getClass().getSimpleName()));
        }

        return Py.py2float(getValue());
    }

}