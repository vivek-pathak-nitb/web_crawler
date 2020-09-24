package com.monzo.webcrawler.filter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HostNameFilterTest {

    private static final String HTTPS_GOOGLE_COM_ABOUT = "https://google.com/about";
    private static final String HTTPS_GOOGLE_COM = "https://google.com";
    private static final String HTTPS_FB_COM = "https://fb.com";
    private HostNameFilter underTest;

    @Before
    public void setup() {
        underTest = new HostNameFilter();
    }

    @Test
    public void isAllowed_sameHostName_true() {
        assertTrue(underTest.isAllowed(HTTPS_GOOGLE_COM_ABOUT, HTTPS_GOOGLE_COM));
    }

    @Test
    public void isAllowed_differentHostName_false() {
        assertFalse(underTest.isAllowed(HTTPS_GOOGLE_COM_ABOUT, HTTPS_FB_COM));
    }
}
