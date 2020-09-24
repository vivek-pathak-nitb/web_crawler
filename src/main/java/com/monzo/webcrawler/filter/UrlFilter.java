package com.monzo.webcrawler.filter;

/**
 * This interface is implemented by various filtering strategies which defines whether a url should be allowed for
 * crawling or not.
 */
public interface UrlFilter {

    boolean isAllowed(final String url,
                      final String hostName);
}
