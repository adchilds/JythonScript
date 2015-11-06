package com.adamchilds.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the {@link StringUtil} class.
 *
 * @author Adam Childs
 * @since 0.2
 */
public class StringUtilTest {

    @Test
    public void testIsBlank() {
        // Null
        assertTrue(StringUtil.isBlank(null));

        // Empty
        assertTrue(StringUtil.isBlank(""));

        // Whitespace
        assertTrue(StringUtil.isBlank("    "));

        // Valid
        assertFalse(StringUtil.isBlank("Test"));
        assertFalse(StringUtil.isBlank("1234"));
        assertFalse(StringUtil.isBlank("!@#$"));
    }

    @Test
    public void testIsNotBlank() {
        // Null
        assertFalse(StringUtil.isNotBlank(null));

        // Empty
        assertFalse(StringUtil.isNotBlank(""));

        // Whitespace
        assertFalse(StringUtil.isNotBlank("    "));

        // Valid
        assertTrue(StringUtil.isNotBlank("Test"));
        assertTrue(StringUtil.isNotBlank("1234"));
        assertTrue(StringUtil.isNotBlank("!@#$"));
    }

}