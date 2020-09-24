package com.monzo.webcrawler.filter;

import com.monzo.webcrawler.utility.Helper;

public class HostNameFilter implements UrlFilter {

    /**
     * Returns true if the hostName in the url matches given hostName.
     */
    @Override
    public boolean isAllowed(final String url,
                             final String hostName) {
        return Helper.isSameHostname(url, hostName);
    }
}
