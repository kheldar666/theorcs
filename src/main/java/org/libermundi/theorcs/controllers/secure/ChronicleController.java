package org.libermundi.theorcs.controllers.secure;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.NewsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChronicleController {

    private final ChronicleService chronicleService;

    private final NewsService newsService;

    public ChronicleController(ChronicleService chronicleService, NewsService newsService) {
        this.chronicleService = chronicleService;
        this.newsService = newsService;
    }

    @GetMapping("/secure/chronicle/create")
    public String create() {
        return "/secure/chronicle/create";
    }

    @GetMapping("/secure/chronicle/{chronicle}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String home(Model model, @PathVariable Chronicle chronicle) {

        return "/secure/chronicle/home";
    }

    @GetMapping("/secure/chronicle/{chronicle}/admin")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String admin(Model model, @PathVariable Chronicle chronicle) {
        return "/secure/chronicle/admin/home";
    }

}
