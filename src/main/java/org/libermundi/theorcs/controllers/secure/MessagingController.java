package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.forms.MessageForm;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class MessagingController {

    private final CharacterService characterService;

    private final SecurityService securityService;

    public MessagingController(CharacterService characterService, SecurityService securityService) {
        this.characterService = characterService;
        this.securityService = securityService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/write")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String write(Model model, @PathVariable Chronicle chronicle) {
        model.addAttribute("newMessage", new MessageForm());
        model.addAttribute("characterList", characterService.getAllCharacters(securityService.getCurrentUser(),chronicle));
        model.addAttribute("contactList", characterService.getAllContacts(securityService.getCurrentUser(),chronicle));
        return "/secure/chronicle/messaging/write";
    }

}
