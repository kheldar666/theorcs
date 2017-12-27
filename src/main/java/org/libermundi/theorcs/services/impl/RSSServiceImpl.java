package org.libermundi.theorcs.services.impl;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.libermundi.theorcs.services.RSSService;
import org.libermundi.theorcs.services.StringFormatService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class RSSServiceImpl implements RSSService {
    private static final String FORUM_FEED = "http://www.liber-mundi.org/forum/feed.php";

    private final StringFormatService stringFormatService;

    public RSSServiceImpl(StringFormatService stringFormatService) {
        this.stringFormatService = stringFormatService;
    }

    @Cacheable("spring")
    public List<SyndEntry> reedFeed() {
        return reedFeed(FORUM_FEED);
    }

    @Cacheable("spring")
    public List<SyndEntry> reedFeed(String url) {
        List<SyndEntry> entries = Lists.newArrayList();
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity().getContent()) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(stream));
                entries.addAll(feed.getEntries());
            } catch (FeedException e) {
                log.error("ERROR : ", e.getMessage());
            }
        } catch (IOException e) {
            log.error("ERROR : ", e.getMessage());
        }
        return entries;
    }

    public String getEntryTitle(SyndEntry entry) {
        // We get the first Content of the Entry.
        SyndContent content = entry.getContents().get(0);

        StringBuilder title = new StringBuilder();
        title.append("<a href=\"");
        title.append(entry.getLink());
        title.append("\" target=\"_blank\">");
        title.append(entry.getTitle());
        title.append("</a>");

        return title.toString();
    }
}
