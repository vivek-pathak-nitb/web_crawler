package com.monzo.webcrawler.utility;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelperTest {

    private static final String HTTPS_GOOGLE_COM_ABOUT = "https://google.com/about";
    private static final String HTTPS_GOOGLE_COM = "https://google.com";
    private static final String HTTP_GOOGLE_COM = "http://google.com";
    private static final String HTTP_GOOGLE_COM_ABOUT = "http://google.com/about";
    private static final String HTTPS_FB_COM = "https://fb.com";

    @Test
    public void getHostName_https() {
        assertEquals(HTTPS_GOOGLE_COM, Helper.getHostname(HTTPS_GOOGLE_COM_ABOUT));
    }

    @Test
    public void getHostName_http() {
        assertEquals(HTTP_GOOGLE_COM, Helper.getHostname(HTTP_GOOGLE_COM_ABOUT));
    }

    @Test
    public void isSameHostName_true() {
        assertTrue(Helper.isSameHostname(HTTPS_GOOGLE_COM_ABOUT, HTTPS_GOOGLE_COM));
    }

    @Test
    public void isSameHostName_false() {
        assertFalse(Helper.isSameHostname(HTTPS_GOOGLE_COM_ABOUT, HTTPS_FB_COM));
    }
}
