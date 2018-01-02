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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class MenuAdvice {

    private final CharacterService characterService;

    private final SecurityService securityService;

    private final ChronicleService chronicleService;

    public MenuAdvice(CharacterService characterService, SecurityService securityService, ChronicleService chronicleService) {
        this.characterService = characterService;
        this.securityService = securityService;
        this.chronicleService = chronicleService;
    }

    @ModelAttribute
    public void addMenus(Model model, HttpServletRequest request) {

        if (log.isDebugEnabled()) {
            log.debug("Loading Navigations");
        }
        HashMap<String,Object> topNav = Maps.newHashMap();

        if(securityService.isLoggedIn()) {
            String currentURI = request.getRequestURI();
            if (request.getRequestURI().contains("/secure/chronicle")) {
                // When the user is within a Chronicle
                String format = "/secure/chronicle/{id}/**";

                String actualUrl = "/secure/chronicle/1/coteries";
                AntPathMatcher pathMatcher = new AntPathMatcher();
                Map<String, String> variables = pathMatcher.extractUriTemplateVariables(format, actualUrl);

                model.addAttribute("_chronicle",chronicleService.findById(Long.valueOf(variables.get("id"))));

            } else {
                //When User is LoggedIn but using the rest of the site
                List<Chronicle> chronicles = characterService.findChronicleByPlayer(securityService.getCurrentUser());

                topNav.put("chronicles", chronicles);
            }

        }



        model.addAttribute("_topnav", topNav);

    }


}
