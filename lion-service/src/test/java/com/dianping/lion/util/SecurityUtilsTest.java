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
        decoded = SecurityUtils.tryDecode("~{74d693d085f3b3507b568141ffb0d0d1d0d0d0c4bca7ba816}");
        assertEquals("dp!@78()-", decoded);
        decoded = SecurityUtils.tryDecode("~{hello}");
        assertEquals("~{hello}", decoded);
    }

}
