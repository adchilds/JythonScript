package com.github.adchilds.jython.serialization;

import org.python.core.PyObject;

/**
 *
 * @param <T>
 */
public abstract class AbstractPyObjectSerializer<T> implements Serializer<T> {

    private final PyObject value;

    protected AbstractPyObjectSerializer(final PyObject value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    protected PyObject getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    protected <T extends PyObject> T getValue(final Class<T> clazz) {
        return clazz.cast(value);
    }

}