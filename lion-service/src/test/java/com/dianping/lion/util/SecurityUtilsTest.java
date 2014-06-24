package com.dianping.lion.util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
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
        
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        for(;;) {
            System.out.print("Input password to encode: ");
            String password = console.readLine();
            if(password.length() == 0) {
                return;
            }
            System.out.println(SecurityUtils.tryDecode(password));
        }
    }

}
