package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.NewsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/news/{news}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String readNews(Model model, @PathVariable Chronicle chronicle, @PathVariable News news) {
        model.addAttribute("news", news);
        return "/secure/chronicle/news";
    }

    @GetMapping("/secure/chronicle/{chronicle}/admin/news")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String admin(Model model, @PathVariable Chronicle chronicle) {
        model.addAttribute("newsList", newsService.getAllNews(chronicle));
        return "/secure/chronicle/admin/news/home";
    }

    @GetMapping("/secure/chronicle/{chronicle}/admin/news/{news}/delete")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String delete(Model model, @PathVariable Chronicle chronicle, @PathVariable News news) {
        newsService.delete(news);
        return "redirect:/secure/chronicle/"+chronicle.getId()+"/admin/news/";
    }

    @GetMapping("/secure/chronicle/{chronicle}/admin/news/{news}/edit")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String edit(Model model, @PathVariable Chronicle chronicle, @PathVariable News news) {
        model.addAttribute("news",news);
        return "/secure/chronicle/admin/news/edit";
    }

    @GetMapping("/secure/chronicle/{chronicle}/admin/news/create")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String create(Model model, @PathVariable Chronicle chronicle) {
        model.addAttribute("news",new News());
        return "/secure/chronicle/admin/news/edit";
    }

    @PostMapping("/secure/chronicle/{chronicle}/admin/news/save")
    @PreAuthorize("hasPermission(#chronicle, 'administration')")
    public String saveOrUpdate(Model model, @PathVariable Chronicle chronicle, @Valid @ModelAttribute("news") News news, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "/secure/chronicle/admin/news/edit";
        }
        newsService.save(news);
        return "redirect:/secure/chronicle/"+chronicle.getId()+"/admin/news/";
    }

}
