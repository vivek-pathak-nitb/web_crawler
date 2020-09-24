package com.monzo.webcrawler;

import com.monzo.webcrawler.crawler.WebCrawler;
import com.monzo.webcrawler.entities.Page;
import com.monzo.webcrawler.extractor.UrlExtractor;
import com.monzo.webcrawler.filter.HostNameFilter;
import com.monzo.webcrawler.filter.UrlFilter;
import com.monzo.webcrawler.gateway.DocumentGateway;
import com.monzo.webcrawler.printer.HierarchyPrinter;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Main application to start crawling.
 */
public class WebCrawlerApplication {

    private static final int DEFAULT_THREADS = 10;
    private static final int DEFAULT_MAX_DEPTH = 4;

    public static void main(final String[] args) {

        if (args == null || args.length == 0) {
            return;
        }

        final String startUrl = args[0];
        final UrlValidator urlValidator = UrlValidator.getInstance();
        if (!urlValidator.isValid(startUrl)) {
            return;
        }

        final int parallelism = getNumberOfThreads(args);
        final int maxDepth = getMaxDepth(args);

        // Add filters
        final List<UrlFilter> urlFilterList = new ArrayList<>();
        urlFilterList.add(new HostNameFilter());

        // start crawling
        final WebCrawler webCrawler = new WebCrawler(new UrlExtractor(new DocumentGateway()), urlFilterList,
                parallelism, maxDepth);
        final Set<Page> pages = webCrawler.crawl(startUrl);

        // print
        final HierarchyPrinter hierarchyPrinter = new HierarchyPrinter();
        hierarchyPrinter.printHierarchy(pages);
    }

    private static int getNumberOfThreads(final String[] args) {
        if (args.length < 2) {
            return DEFAULT_THREADS;
        }

        return Integer.parseInt(args[1]);
    }

    private static int getMaxDepth(final String[] args) {
        if (args.length < 3) {
            return DEFAULT_MAX_DEPTH;
        }

        return Integer.parseInt(args[2]);
    }
}
