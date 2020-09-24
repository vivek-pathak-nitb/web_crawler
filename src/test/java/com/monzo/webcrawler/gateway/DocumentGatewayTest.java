package com.monzo.webcrawler.gateway;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class DocumentGatewayTest {

    private static final String URL = "https://monzo.com";

    private DocumentGateway underTest;

    @Before
    public void setup() {
        underTest = new DocumentGateway();
    }

    @Test
    public void getDocument_validDocument_noException() throws IOException {
        assertNotNull(underTest.getDocument(URL));
    }
}
