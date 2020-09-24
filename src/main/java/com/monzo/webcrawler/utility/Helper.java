package com.monzo.webcrawler.utility;

public class Helper {

    private static final String HTTPS = "https";
    private static final char SLASH = '/';

    /**
     * Given a url returns hostname.
     */
    public static String getHostname(final String url) {
        final int fromIndex = url.startsWith(HTTPS) ? 8 : 7;
        final int idx = url.indexOf(SLASH, fromIndex);
        return (idx != -1) ? url.substring(0, idx) : url;
    }

    /**
     * Checks if url belongs to same hostName.
     */
    public static boolean isSameHostname(final String url,
                                         final String hostname) {
        if (!url.startsWith(hostname)) {
            return false;
        }
        return url.length() == hostname.length() || url.charAt(hostname.length()) == SLASH;
    }
}
