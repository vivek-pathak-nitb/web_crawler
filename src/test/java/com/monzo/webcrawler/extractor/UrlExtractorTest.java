package com.monzo.webcrawler.extractor;

import com.monzo.webcrawler.entities.Page;
import com.monzo.webcrawler.gateway.DocumentGateway;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UrlExtractorTest {

    private static final String MAIN_URL = "https://monzo.com";
    private static final String URL_1 = "https://monzo.com/about/";
    private static final String URL_2 = "https://monzo.com/career/";
    private DocumentGateway mockDocumentGateway;
    private UrlExtractor underTest;

    @Before
    public void setup() {
        mockDocumentGateway = mock(DocumentGateway.class);
        underTest = new UrlExtractor(mockDocumentGateway);
    }

    @Test
    public void getUrls_ioException_emptyUrlsSet() throws IOException {
        // set up
        when(mockDocumentGateway.getDocument(anyString())).thenThrow(new IOException(""));

        // execute
        final Page page = new Page(MAIN_URL, 0, null);
        final Set<Page> pages = underTest.getUrls(page);

        // assert
        assertTrue(pages.isEmpty());
        verify(mockDocumentGateway, times(1)).getDocument(anyString());
    }


    @Test
    public void getUrls_nonEmptyUrlsSet() throws IOException {
        // set up
        final Document mockDocument = mock(Document.class);
        when(mockDocumentGateway.getDocument(anyString())).thenReturn(mockDocument);

        final Elements elements = mock(Elements.class);
        when(mockDocument.select(eq(UrlExtractor.A_HREF_SELECTOR))).thenReturn(elements);

        final Element element1 = mock(Element.class);
        when(element1.attr(eq(UrlExtractor.ABS_HREF))).thenReturn(URL_1);
        final Element element2 = mock(Element.class);
        when(element2.attr(eq(UrlExtractor.ABS_HREF))).thenReturn(URL_2);

        final List<Element> elementList = new ArrayList<>();
        elementList.add(element1);
        elementList.add(element2);

        when(elements.iterator()).thenReturn(elementList.iterator());

        // execute
        final Page page = new Page(MAIN_URL, 0, null);
        final Set<Page> pages = underTest.getUrls(page);

        // assert
        assertFalse(pages.isEmpty());
        assertEquals(2, pages.size());
        verify(mockDocumentGateway).getDocument(anyString());
        verify(elements, times(1)).iterator();
        verify(mockDocument).select(eq(UrlExtractor.A_HREF_SELECTOR));
        verify(element1, times(2)).attr(eq(UrlExtractor.ABS_HREF));
        verify(element2, times(2)).attr(eq(UrlExtractor.ABS_HREF));
    }
}
