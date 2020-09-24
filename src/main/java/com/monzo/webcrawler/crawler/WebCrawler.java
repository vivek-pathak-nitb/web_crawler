package com.monzo.webcrawler.crawler;

import com.monzo.webcrawler.entities.Page;
import com.monzo.webcrawler.extractor.UrlExtractor;
import com.monzo.webcrawler.filter.UrlFilter;
import com.monzo.webcrawler.utility.Helper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for crawling a page to a certain depth as specified during initialisation.
 */
public class WebCrawler {

    private final UrlExtractor urlExtractor;
    private final List<UrlFilter> urlFilters;
    private final ForkJoinPool threadPool;
    private final int maxDepth;

    public WebCrawler(final UrlExtractor urlExtractor,
                      final List<UrlFilter> urlFilters,
                      final int parallelism,
                      final int maxDepth) {
        this.urlExtractor = urlExtractor;
        this.urlFilters = urlFilters;
        this.threadPool = new ForkJoinPool(parallelism);
        this.maxDepth = maxDepth;
    }

    /**
     * Crawls a given url.
     *
     * @param startUrl Url which need to be crawled.
     * @return set of crawled url's.
     */
    public Set<Page> crawl(final String startUrl) {
        final String hostname = Helper.getHostname(startUrl);
        final Set<Page> visited = ConcurrentHashMap.newKeySet();
        final Page startPage = new Page(startUrl, 0, null);
        visited.add(startPage);
        return crawl(startPage, hostname, visited)
                .collect(Collectors.toSet());
    }

    private Stream<Page> crawl(final Page startPage,
                               final String hostname,
                               final Set<Page> visited) {
        try {
            final Stream<Page> stream = threadPool.submit(() -> urlExtractor.getUrls(startPage)
                    .parallelStream()
                    .filter(page -> shouldCrawl(page, hostname))
                    .filter(visited::add)
                    .flatMap(page -> crawl(page, hostname, visited))).get();
            return Stream.concat(Stream.of(startPage), stream);
        } catch (final InterruptedException | ExecutionException ex) {
            return Stream.of(startPage);
        }
    }

    private boolean shouldCrawl(final Page page,
                                final String hostName) {
        if (page.getDepth() > maxDepth) {
            return false;
        }
        for (final UrlFilter urlFilter : urlFilters) {
            if (!urlFilter.isAllowed(page.getUrl(), hostName)) {
                return false;
            }
        }
        return true;
    }
}
