package com.monzo.webcrawler.extractor;

import com.monzo.webcrawler.entities.Page;
import com.monzo.webcrawler.gateway.DocumentGateway;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * Responsible for extracting valid url's from a web page.
 */
public class UrlExtractor {

    public static final String A_HREF_SELECTOR = "a[href]";
    public static final String ABS_HREF = "abs:href";

    private final DocumentGateway documentGateway;
    private final UrlValidator urlValidator;

    public UrlExtractor(final DocumentGateway documentGateway) {
        this.documentGateway = documentGateway;
        this.urlValidator = UrlValidator.getInstance();
    }

    public Set<Page> getUrls(final Page page) {
        final Optional<Document> documentOptional = getDocument(page.getUrl());
        if (!documentOptional.isPresent()) {
            return new HashSet<>();
        }

        final Document document = documentOptional.get();
        final Elements links = document.select(A_HREF_SELECTOR);
        final Set<Page> lists = new HashSet<>();
        for (final Element link : links) {
            if (urlValidator.isValid(link.attr(ABS_HREF))) {
                lists.add(new Page(link.attr(ABS_HREF), page.getDepth() + 1, page));
            }
        }
        return lists;
    }

    private Optional<Document> getDocument(final String url) {
        try {
            return Optional.of(documentGateway.getDocument(url));
        } catch (final IOException ex) {
            return Optional.empty();
        }
    }
}
