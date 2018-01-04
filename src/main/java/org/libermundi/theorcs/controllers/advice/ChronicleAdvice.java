package org.libermundi.theorcs.controllers.advice;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.SecurityService;
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
public class ChronicleAdvice {

    private final SecurityService securityService;

    private final ChronicleService chronicleService;

    public ChronicleAdvice(SecurityService securityService, ChronicleService chronicleService) {
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
            if (currentURI.contains("/secure/chronicle")) {
                // When the user is within a Chronicle
                String format = "/secure/chronicle/{id}/**";

                AntPathMatcher pathMatcher = new AntPathMatcher();
                Map<String, String> variables = pathMatcher.extractUriTemplateVariables(format, currentURI);

                model.addAttribute("_chronicle",chronicleService.findById(Long.valueOf(variables.get("id"))));

            }

        }

        model.addAttribute("_topnav", topNav);

    }

}
