package org.libermundi.theorcs.services.impl;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import org.libermundi.theorcs.services.FeedFormatService;
import org.libermundi.theorcs.services.StringFormatService;
import org.springframework.stereotype.Service;

@Service
public class FeedFormatServiceImpl implements FeedFormatService {

    private final StringFormatService stringFormatService;

    public FeedFormatServiceImpl(StringFormatService stringFormatService) {
        this.stringFormatService = stringFormatService;
    }

    public String getEntryTitle(SyndEntry entry) {
        // We get the first Content of the Entry.
        SyndContent content = entry.getContents().get(0);

        StringBuilder title = new StringBuilder();
        title.append(entry.getTitle());
        title.append(" (");
        title.append(entry.getAuthor());
        title.append(") - ");
        title.append(stringFormatService.formatDate(entry.getPublishedDate()));

        return title.toString();
    }
}
