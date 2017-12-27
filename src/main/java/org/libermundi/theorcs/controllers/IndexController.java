package org.libermundi.theorcs.controllers;

import com.rometools.rome.feed.synd.SyndEntry;
import org.libermundi.theorcs.services.RSSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Controller
public class IndexController {
    private final RSSService rssService;

    public IndexController(RSSService rssService) {
        this.rssService = rssService;
    }

    @GetMapping({"","/","/index"})
    public String getIndexPage(Model model) {
        Date start = new Date();
        List<SyndEntry> entries = rssService.reedFeed();
        Date end = new Date();
        long delta = end.getTime() - start.getTime();

        model.addAttribute("feedEntries",entries);

        return "index";
    }

}
