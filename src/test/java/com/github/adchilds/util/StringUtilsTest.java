package com.github.adchilds.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link StringUtils} class.
 *
 * @author Adam Childs
 * @since 1.1
 */
class StringUtilsTest {

    @Test
    void testIsBlank_null() {
        assertTrue(StringUtils.isBlank(null));
    }

    @Test
    void testIsBlank_empty() {
        assertTrue(StringUtils.isBlank(""));
    }

    @Test
    void testIsBlank_whitespace() {
        assertTrue(StringUtils.isBlank("    "));
        assertTrue(StringUtils.isBlank("\t"));
        assertTrue(StringUtils.isBlank("\r"));
        assertTrue(StringUtils.isBlank("\n"));
    }

    @Test
    void testIsBlank_invalid() {
        assertFalse(StringUtils.isBlank("Test"));
        assertFalse(StringUtils.isBlank("1234"));
        assertFalse(StringUtils.isBlank("!@#$"));
    }

    @Test
    void testIsNotBlank_null() {
        assertFalse(StringUtils.isNotBlank(null));
    }

    @Test
    void testIsNotBlank_empty() {
        assertFalse(StringUtils.isNotBlank(""));

    }

    @Test
    void testIsNotBlank_whitespace() {
        assertFalse(StringUtils.isNotBlank("    "));
        assertFalse(StringUtils.isNotBlank("\t"));
        assertFalse(StringUtils.isNotBlank("\r"));
        assertFalse(StringUtils.isNotBlank("\n"));
    }

    @Test
    void testIsNotBlank_valid() {
        assertTrue(StringUtils.isNotBlank("Test"));
        assertTrue(StringUtils.isNotBlank("1234"));
        assertTrue(StringUtils.isNotBlank("!@#$"));
    }

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<StringUtils> constructor = StringUtils.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}