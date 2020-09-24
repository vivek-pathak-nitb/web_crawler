package com.monzo.webcrawler.entities;

import java.util.Objects;

/**
 * Represents a page for the purpose of crawling..
 */
public class Page {

    private String url;
    private int depth;
    private Page parent;

    public Page(final String url,
                final int depth,
                final Page parent) {
        this.url = url;
        this.depth = depth;
        this.parent = parent;
    }

    public String getUrl() {
        return url;
    }

    public int getDepth() {
        return depth;
    }

    public Page getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page page = (Page) o;
        return Objects.equals(url, page.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
