package com.monzo.webcrawler.crawler;

import com.monzo.webcrawler.entities.Page;
import com.monzo.webcrawler.extractor.UrlExtractor;
import com.monzo.webcrawler.filter.HostNameFilter;
import com.monzo.webcrawler.filter.UrlFilter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class WebCrawlerTest {

    private static final String START_URL = "https://monzo.com";
    private static final String URL_1 = "https://monzo.com/about/";
    private static final String URL_2 = "https://monzo.com/career/";

    private UrlExtractor mockUrlExtractor;
    private WebCrawler underTest;

    @Before
    public void setup() {
        mockUrlExtractor = mock(UrlExtractor.class);
        final HostNameFilter hostNameFilter = new HostNameFilter();
        final List<UrlFilter> urlFilterList = new ArrayList<>();
        urlFilterList.add(hostNameFilter);
        underTest = new WebCrawler(mockUrlExtractor, urlFilterList, 2, 2);
    }

    @Test
    public void crawl() {
        // set up
        final Page page = new Page(START_URL, 0, null);
        final Page page1 = new Page(URL_1, 1, page);
        final Page page2 = new Page(URL_2, 1, page);
        when(mockUrlExtractor.getUrls(eq(page))).thenReturn(new HashSet<>(Arrays.asList(page1, page2)));
        when(mockUrlExtractor.getUrls(eq(page1))).thenReturn(new HashSet<>());
        when(mockUrlExtractor.getUrls(eq(page2))).thenReturn(new HashSet<>());

        // execute
        final Set<Page> pages = underTest.crawl(START_URL);

        // assert
        assertFalse(pages.isEmpty());
        assertEquals(3, pages.size());
        verify(mockUrlExtractor).getUrls(eq(page));
        verify(mockUrlExtractor).getUrls(eq(page1));
        verify(mockUrlExtractor).getUrls(eq(page2));
    }
}
