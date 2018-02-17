package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.forms.CharacterForm;
import org.libermundi.theorcs.forms.MessageForm;
import org.libermundi.theorcs.forms.PostForm;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.PostService;
import org.libermundi.theorcs.services.SceneService;
import org.libermundi.theorcs.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
public class ScenesController {
    private final SceneService sceneService;
    private final PostService postService;
    private final CharacterService characterService;
    private final SecurityService securityService;

    public ScenesController(SceneService sceneService, PostService postService,
                            CharacterService characterService, SecurityService securityService) {
        this.sceneService = sceneService;
        this.postService = postService;
        this.characterService = characterService;
        this.securityService = securityService;
    }

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

    @GetMapping("/secure/chronicle/{chronicle}/scene/{scene}/post/write")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String write(Model model, @PathVariable Chronicle chronicle,
                        @PathVariable Scene scene, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        if(!model.containsAttribute("newPost")) { // Can happen when there is a Mod
            PostForm form = new PostForm();
            form.setRegisterToScene(currentCharacter.getRegisteredScenes().contains(scene));

            model.addAttribute("newPost", form);
        }

        model.addAttribute("scene", scene);
        model.addAttribute("characterList", characterService.getAllCharacters(securityService.getCurrentUser(),chronicle));

        return "/secure/chronicle/post/write";
    }

    @PostMapping("/secure/chronicle/{chronicle}/post/send")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String send(Model model, @PathVariable Chronicle chronicle,
                       @Valid @ModelAttribute("newPost") PostForm postForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "/secure/chronicle/post/write";
        }

        postService.sendPost(postForm);

        return "redirect:/secure/chronicle/" + chronicle.getId() + "/scene/" + postForm.getScene().getId();
    }
}
