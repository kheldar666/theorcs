package org.libermundi.theorcs.controllers.advice;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.SecurityService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    public void addMenus(Model model) {

        if (log.isDebugEnabled()) {
            log.debug("Loading Navigations");
        }
        HashMap<String,Object> topNav = Maps.newHashMap();

        if(securityService.isLoggedIn()){
            List<Chronicle> chronicles = characterService.findChronicleByPlayer(securityService.getCurrentUser());

            topNav.put("chronicles", chronicles);
        }


        model.addAttribute("_topnav", topNav);

    }
}
