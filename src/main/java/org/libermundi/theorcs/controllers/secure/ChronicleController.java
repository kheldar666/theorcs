package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.chronicle.News;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.GameService;
import org.libermundi.theorcs.services.NewsService;
import org.libermundi.theorcs.services.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ChronicleController {

    private final ChronicleService chronicleService;

    private final GameService gameService;

    private final NewsService newsService;

    private final SecurityService securityService;

    public ChronicleController(ChronicleService chronicleService, GameService gameService,
                               NewsService newsService, SecurityService securityService) {
        this.chronicleService = chronicleService;
        this.gameService = gameService;
        this.newsService = newsService;
        this.securityService = securityService;
    }

    @GetMapping("/secure/chronicle/create")
    public String create(Model model) {
        model.addAttribute("newChronicle", chronicleService.createNew());
        model.addAttribute("gameList", gameService.findAll());
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

    @PostMapping(value = "/secure/chronicle/create")
    public String saveOrUpdate(Model model, @Valid @ModelAttribute("newChronicle") Chronicle chronicle,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "/secure/chronicle/admin/news/edit";
        }

        chronicle.setAdmin(securityService.getCurrentUser());
        chronicle.setOpenForInscription(Boolean.FALSE);

        chronicleService.save(chronicle);
        return "redirect:/secure/index";
    }


}
