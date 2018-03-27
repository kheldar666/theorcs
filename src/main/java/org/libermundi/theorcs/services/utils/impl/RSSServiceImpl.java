package org.libermundi.theorcs.services.utils.impl;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.libermundi.theorcs.services.utils.RSSService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service("RSSService")
@NoArgsConstructor
public class RSSServiceImpl implements RSSService {

    @Cacheable("spring")
    public List<SyndEntry> reedFeed(String url) {
        List<SyndEntry> entries = Lists.newArrayList();
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpUriRequest request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity().getContent()) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(stream));
                entries.addAll(feed.getEntries());
                if(log.isDebugEnabled()){
                    log.debug("Successfully loaded {} entries from feed at : {}", entries.size(), url);
                }
            } catch (FeedException e) {
                log.error("Error while Loading Feeds: " + e.getMessage());
            }
        } catch (IOException e) {
            log.error("Error while Loading Feeds: " + e.getMessage());
        }
        return entries;
    }

    public String getEntryTitle(SyndEntry entry) {

        StringBuilder title = new StringBuilder();
            title.append("<a href=\"");
            title.append(entry.getLink());
            title.append("\" target=\"_blank\">");
            title.append(entry.getTitle());
            title.append("</a>");

        return title.toString();
    }
}
