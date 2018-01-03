package org.libermundi.theorcs.controllers.secure;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.NewsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/admin/news")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String admin(Model model, @PathVariable Chronicle chronicle) {
        model.addAttribute("newsList", newsService.getAllNews(chronicle));
        return "/secure/chronicle/admin/news/home";
    }
}
