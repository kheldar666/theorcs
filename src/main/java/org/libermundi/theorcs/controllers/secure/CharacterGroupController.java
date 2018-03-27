package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.chronicle.CharacterGroupService;
import org.libermundi.theorcs.services.chronicle.CharacterService;
import org.libermundi.theorcs.services.scene.PostService;
import org.libermundi.theorcs.services.scene.SceneService;
import org.libermundi.theorcs.services.security.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class CharacterGroupController {
    private final SceneService sceneService;
    private final PostService postService;
    private final CharacterService characterService;
    private final SecurityService securityService;
    private final CharacterGroupService characterGroupService;

    public CharacterGroupController(SceneService sceneService, PostService postService,
                                    CharacterService characterService, SecurityService securityService,
                                    CharacterGroupService characterGroupService) {
        this.sceneService = sceneService;
        this.postService = postService;
        this.characterService = characterService;
        this.securityService = securityService;
        this.characterGroupService = characterGroupService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/groups")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String showScenesList(Model model, @PathVariable Chronicle chronicle, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        model.addAttribute("groups", characterGroupService.getAllCharacterGroup(chronicle, currentCharacter));

        return "/secure/chronicle/characterGroup/allGroups";
    }
}
