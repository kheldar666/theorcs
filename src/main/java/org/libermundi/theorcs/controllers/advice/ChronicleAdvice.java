package org.libermundi.theorcs.controllers.advice;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.ChronicleService;
import org.libermundi.theorcs.services.SecurityService;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ChronicleAdvice {

    private final SecurityService securityService;

    private final ChronicleService chronicleService;

    private final CharacterService characterService;

    public ChronicleAdvice(SecurityService securityService, ChronicleService chronicleService,
                           CharacterService characterService) {
        this.securityService = securityService;
        this.chronicleService = chronicleService;
        this.characterService = characterService;
    }

    @ModelAttribute
    public void addMenus(Model model, HttpServletRequest request, HttpSession session) {

        if (log.isDebugEnabled()) {
            log.debug("Loading Chronicle Parameters");
        }


        if(securityService.isLoggedIn()) {
            String currentURI = request.getRequestURI();
            if (currentURI.contains("/secure/chronicle")) {
                // When the user is within a Chronicle
                String format = "/secure/chronicle/{id}/**";

                AntPathMatcher pathMatcher = new AntPathMatcher();
                Map<String, String> variables = pathMatcher.extractUriTemplateVariables(format, currentURI);

                String id = variables.get("id");

                if(StringUtils.isNumeric(id)) {
                    Chronicle currentChronicle = chronicleService.findById(Long.valueOf(id));

                    model.addAttribute("_chronicle",currentChronicle);

                    if(session.getAttribute("_currentCharacter") != null) {
                        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");
                        if (!currentCharacter.getChronicle().equals(currentChronicle)){
                            session.removeAttribute("_currentCharacter");
                        }
                    }

                    if(session.getAttribute("_currentCharacter") == null) {
                        User player = securityService.getCurrentUser();
                        session.setAttribute("_currentCharacter",characterService.getDefaultCharacter(player,currentChronicle));
                    }

                }


            }

        }

    }

}
