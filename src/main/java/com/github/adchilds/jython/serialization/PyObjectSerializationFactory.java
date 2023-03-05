package com.github.adchilds.jython.serialization;

import org.python.core.PyObject;

/**
 *
 */
public interface PyObjectSerializationFactory {

    /**
     *
     * @param object
     * @return
     * @param <T>
     */
    <T extends PyObject> Serializer<?> getSerializer(T object);

}