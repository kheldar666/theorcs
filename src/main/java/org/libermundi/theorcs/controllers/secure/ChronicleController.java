package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.forms.ChronicleForm;
import org.libermundi.theorcs.services.chronicle.ChronicleService;
import org.libermundi.theorcs.services.chronicle.GameService;
import org.libermundi.theorcs.services.security.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class ChronicleController {

    private final ChronicleService chronicleService;

    private final GameService gameService;

    private final SecurityService securityService;

    public ChronicleController(ChronicleService chronicleService, GameService gameService, SecurityService securityService) {
        this.chronicleService = chronicleService;
        this.gameService = gameService;
        this.securityService = securityService;
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

    @GetMapping("/secure/chronicle/create")
    public String create(Model model) {
        model.addAttribute("chronicleForm", new ChronicleForm());
        model.addAttribute("gameList", gameService.findAll());
        return "/secure/chronicle/create";
    }

    @PostMapping(value = "/secure/chronicle/create")
    public String saveOrUpdate(Model model, @Valid @ModelAttribute("chronicleForm") ChronicleForm chronicleForm,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "/secure/chronicle/create";
        }

        Chronicle newChronicle = chronicleService.createNew();

        newChronicle.setTitle(chronicleForm.getTitle());
        newChronicle.setSubTitle(chronicleForm.getSubTitle());
        newChronicle.setDescription(chronicleForm.getDescription());
        newChronicle.setType(chronicleForm.getType());
        newChronicle.setGenre(chronicleForm.getGenre());
        newChronicle.setStyle(chronicleForm.getStyle());
        newChronicle.setPace(chronicleForm.getPace());
        newChronicle.setPassword(chronicleForm.getPassword());
        newChronicle.setDate(chronicleForm.getDate());
        newChronicle.setBackground(chronicleForm.getBackground());
        newChronicle.setGame(chronicleForm.getGame());

        newChronicle.setAdmin(securityService.getCurrentUser());
        newChronicle.setOpenForInscription(Boolean.FALSE);

        chronicleService.save(newChronicle);
        return "redirect:/secure/index";
    }


}
