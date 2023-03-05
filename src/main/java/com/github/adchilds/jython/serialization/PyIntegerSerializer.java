package com.github.adchilds.jython.serialization;

import com.github.adchilds.jython.exception.JythonScriptRuntimeException;
import org.python.core.Py;
import org.python.core.PyInteger;
import org.python.core.PyObject;

/**
 *
 */
public class PyIntegerSerializer extends AbstractPyObjectSerializer<Integer> {

    protected PyIntegerSerializer(final PyObject value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer serialize() {
        if (getValue() == null) {
            return null;
        }

        if (!(getValue() instanceof PyInteger)) {
            throw new JythonScriptRuntimeException(String.format("Cannot serialize type [%s] as Integer.",
                    getValue().getClass().getSimpleName()));
        }

        return Py.py2int(getValue());
    }

}