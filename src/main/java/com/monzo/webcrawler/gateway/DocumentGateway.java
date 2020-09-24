package com.monzo.webcrawler.gateway;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Fetches a web page and returns an instance of {@link Document}.
 */
public class DocumentGateway {

    public Document getDocument(final String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
