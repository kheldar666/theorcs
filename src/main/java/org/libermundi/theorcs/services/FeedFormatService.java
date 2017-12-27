package org.libermundi.theorcs.services;

import com.rometools.rome.feed.synd.SyndEntry;

public interface FeedFormatService {
    String getEntryTitle(SyndEntry entry);
}
