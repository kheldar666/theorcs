package org.libermundi.theorcs.controllers.advice;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.chronicle.CharacterService;
import org.libermundi.theorcs.services.security.SecurityService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Slf4j
@ControllerAdvice
public class MenuAdvice {

    private final CharacterService characterService;

    private final SecurityService securityService;

    public MenuAdvice(CharacterService characterService, SecurityService securityService) {
        this.characterService = characterService;
        this.securityService = securityService;
    }

    @ModelAttribute
    public void addMenus(Model model, HttpServletRequest request) {

        if (log.isDebugEnabled()) {
            log.debug("Loading Navigations");
        }
        HashMap<String,Object> topNav = Maps.newHashMap();

        if(securityService.isLoggedIn()) {
            String currentURI = request.getRequestURI();
            if (currentURI.contains("/secure/chronicle") && !currentURI.equals("/secure/chronicle/create")) {
                // When the user is within a Chronicle


            } else {
                //When User is LoggedIn but using the rest of the site
                List<Chronicle> chronicles = characterService.findChronicleByPlayer(securityService.getCurrentUser());

                topNav.put("chronicles", chronicles);
            }

        }

        model.addAttribute("_topnav", topNav);

    }
}
