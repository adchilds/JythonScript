package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyObject;

/**
 *
 */
public class PyBooleanSerializer extends AbstractPyObjectSerializer<Boolean> {

    protected PyBooleanSerializer(final PyObject value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean serialize() {
        if (getValue() == null) {
            return null;
        }

        if (!(getValue() instanceof PyBoolean)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Boolean.",
                    getValue().getClass().getSimpleName()));
        }

        return Py.py2boolean(getValue());
    }

}