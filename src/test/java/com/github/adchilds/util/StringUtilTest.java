package com.github.adchilds.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * Tests for the {@link StringUtil} class.
 *
 * @author Adam Childs
 * @since 1.1
 */
public class StringUtilTest {

    @Test
    public void testIsBlank_null() {
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlank_empty() {
        assertTrue(StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlank_whitespace() {
        assertTrue(StringUtil.isBlank("    "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\r"));
        assertTrue(StringUtil.isBlank("\n"));
    }

    @Test
    public void testIsBlank_invalid() {
        assertFalse(StringUtil.isBlank("Test"));
        assertFalse(StringUtil.isBlank("1234"));
        assertFalse(StringUtil.isBlank("!@#$"));
    }

    @Test
    public void testIsNotBlank_null() {
        assertFalse(StringUtil.isNotBlank(null));
    }

    @Test
    public void testIsNotBlank_empty() {
        assertFalse(StringUtil.isNotBlank(""));

    }

    @Test
    public void testIsNotBlank_whitespace() {
        assertFalse(StringUtil.isNotBlank("    "));
        assertFalse(StringUtil.isNotBlank("\t"));
        assertFalse(StringUtil.isNotBlank("\r"));
        assertFalse(StringUtil.isNotBlank("\n"));
    }

    @Test
    public void testIsNotBlank_valid() {
        assertTrue(StringUtil.isNotBlank("Test"));
        assertTrue(StringUtil.isNotBlank("1234"));
        assertTrue(StringUtil.isNotBlank("!@#$"));
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<StringUtil> constructor = StringUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}