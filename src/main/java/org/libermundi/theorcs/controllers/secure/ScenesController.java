package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.forms.CharacterForm;
import org.libermundi.theorcs.services.PostService;
import org.libermundi.theorcs.services.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class ScenesController {
    @Autowired
    private SceneService sceneService;
    @Autowired
    private PostService postService;

    @GetMapping("/secure/chronicle/{chronicle}/scenes")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String showScenesList(Model model, @PathVariable Chronicle chronicle, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        model.addAttribute("scenes", sceneService.getAllScenes(chronicle));

        return "/secure/chronicle/scene/allScenes";
    }

    @GetMapping("/secure/chronicle/{chronicle}/scene/{scene}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String showScene(Model model, @PathVariable Chronicle chronicle,
                             @PathVariable Scene scene, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        model.addAttribute("posts", postService.getAllPosts(scene));

        return "/secure/chronicle/scene/show";
    }
}
