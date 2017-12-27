package org.libermundi.theorcs.controllers;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import org.libermundi.theorcs.services.FeedFormatService;
import org.libermundi.theorcs.services.RSSService;
import org.libermundi.theorcs.services.StringFormatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Date;
import java.util.List;

@Controller
public class IndexController {
    private final FeedFormatService feedFormatService;

    private final RSSService rssService;

    public IndexController(FeedFormatService feedFormatService, RSSService rssService) {
        this.feedFormatService = feedFormatService;
        this.rssService = rssService;
    }

    @GetMapping({"","/","/index"})
    public String getIndexPage(Model model) {
        Date start = new Date();
        System.out.println("Start ==================>" + start);
        List<SyndEntry> entries = rssService.reedFeed();
        Date end = new Date();
        System.out.println("End ==================>" + end);
        long delta = end.getTime() - start.getTime();
        System.out.println("Delta ==================>" + delta);


        model.addAttribute("feedEntries",entries);

        return "index";
    }

}
