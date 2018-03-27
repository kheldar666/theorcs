package org.libermundi.theorcs.services.utils;

import com.rometools.rome.feed.synd.SyndEntry;

import java.util.List;

public interface RSSService {

    List<SyndEntry> reedFeed(String url);

    String getEntryTitle(SyndEntry entry);

}
