package com.monzo.webcrawler.printer;

import com.monzo.webcrawler.entities.Page;

import java.util.*;

/**
 * Prints crawled url in an hierarchy.
 */
public class HierarchyPrinter {

    public void printHierarchy(final Set<Page> pages) {
        final Set<Page> leafPages = getLeafPages(pages);
        final Map<String, Node> nodeMap = new HashMap<>();
        Node[] root = new Node[1];
        createTree(leafPages, root, nodeMap);
        print(root[0], "");
    }

    private void print(final Node root, final String appender) {
        System.out.println(appender + " " + root.getUrl());
        for (final Node node : root.getChildren()) {
            print(node, appender + "-");
        }
    }

    private void createTree(Set<Page> pages,
                            Node[] root,
                            Map<String, Node> nodeMap) {
        if (pages.isEmpty()) {
            return;
        }

        final Set<Page> parentPages = new HashSet<>();
        for (final Page page : pages) {
            final Node node = nodeMap.getOrDefault(page.getUrl(), new Node(page.getUrl()));
            nodeMap.putIfAbsent(page.getUrl(), node);
            if (page.getParent() != null) {
                final Page parentPage = page.getParent();
                final Node parentNode = nodeMap.getOrDefault(parentPage.getUrl(), new Node(parentPage.getUrl()));
                nodeMap.putIfAbsent(parentPage.getUrl(), parentNode);
                parentNode.getChildren().add(node);
                parentPages.add(page.getParent());
            } else {
                root[0] = node;
            }
        }
        createTree(parentPages, root, nodeMap);
    }

    private Set<Page> getLeafPages(final Set<Page> pages) {
        final int maxDepth = getMaxDepth(pages);
        final Set<Page> leafPages = new HashSet<>();
        for (final Page page : pages) {
            if (page.getDepth() == maxDepth) {
                leafPages.add(page);
            }
        }
        return leafPages;
    }

    private int getMaxDepth(final Set<Page> pages) {
        int maxDepth = 0;
        for (final Page page : pages) {
            maxDepth = Math.max(maxDepth, page.getDepth());
        }
        return maxDepth;
    }

    private static class Node {
        private final String url;
        private final Set<Node> children;

        public Node(final String url) {
            this.url = url;
            this.children = new HashSet<>();
        }

        public String getUrl() {
            return url;
        }

        public Set<Node> getChildren() {
            return children;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(getUrl(), node.getUrl());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUrl());
        }
    }
}
