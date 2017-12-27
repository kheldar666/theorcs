package org.libermundi.theorcs.services;

import com.rometools.rome.feed.synd.SyndEntry;

import java.util.List;

public interface RSSService {

    List<SyndEntry> reedFeed();

    List<SyndEntry> reedFeed(String url);

}
