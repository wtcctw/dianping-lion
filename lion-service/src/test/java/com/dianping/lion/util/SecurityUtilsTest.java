package com.dianping.lion.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

public class SecurityUtilsTest {

    @Test
    public void testTryDecode() throws IOException {
        String decoded = SecurityUtils.tryDecode(null);
        assertNull(decoded);
        decoded = SecurityUtils.tryDecode("");
        assertEquals("", decoded);
        decoded = SecurityUtils.tryDecode("hello");
        assertEquals("hello", decoded);
        decoded = SecurityUtils.tryDecode("~{d3b4f5e2a4b3c1a0622267e0b011b61150b0b0a5f913267ad}");
        assertEquals("dp!@78()-=", decoded);
        decoded = SecurityUtils.tryDecode("~{hello}");
        assertEquals("~{hello}", decoded);
    }

}
