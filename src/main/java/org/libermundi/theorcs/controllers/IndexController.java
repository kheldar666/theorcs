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

    @GetMapping({"","/","/index"})
    public String getIndexPage(Model model) {
        return "index";
    }

}
