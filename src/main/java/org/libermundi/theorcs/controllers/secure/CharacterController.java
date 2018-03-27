package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.utils.Picture;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.forms.CharacterForm;
import org.libermundi.theorcs.services.chronicle.CharacterService;
import org.libermundi.theorcs.services.utils.PictureService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
public class CharacterController {

    private CharacterService characterService;
    private PictureService pictureService;

    public CharacterController(CharacterService characterService, PictureService pictureService) {
        this.characterService = characterService;
        this.pictureService = pictureService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/character/background")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String showBackground(@PathVariable Chronicle chronicle, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        return "/secure/chronicle/character/background";
    }

    @GetMapping("/secure/chronicle/{chronicle}/character/description")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String showDescription(Model model, @PathVariable Chronicle chronicle, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        if(!model.containsAttribute("descriptionForm")) {
            CharacterForm characterForm = new CharacterForm();
            characterForm.setDescription(currentCharacter.getDescription());
            characterForm.setSignature(currentCharacter.getSignature());
            model.addAttribute("descriptionForm", characterForm);
        }

        return "/secure/chronicle/character/description";
    }

    @PostMapping("/secure/chronicle/{chronicle}/character/description")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String updateDescription(Model model, @PathVariable Chronicle chronicle,
                       @Valid @ModelAttribute("description") CharacterForm descriptionForm, BindingResult bindingResult, HttpSession session) {
        if(descriptionForm.getDescription().isEmpty()) {
            ObjectError error = new ObjectError("description","You need to write a description");
            bindingResult.addError(error);
        }

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "/secure/chronicle/" + chronicle.getId() + "/character/description";
        }

        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");
        currentCharacter.setDescription(descriptionForm.getDescription());
        currentCharacter.setSignature(descriptionForm.getSignature());

        Picture toDelete = null;
        if(null != descriptionForm.getProfilePicture()) {
            Picture profilePicture = pictureService.savePicture(descriptionForm.getProfilePicture());
            if(null != profilePicture && null != currentCharacter.getAvatar()) {
                toDelete = currentCharacter.getAvatar();
                currentCharacter.setAvatar(profilePicture);
            }
        }

        characterService.save(currentCharacter);

        // If picture is changed, we must delete the old picture
        if(null != toDelete) {
            pictureService.delete(toDelete);
        }

        return "redirect:/secure/chronicle/" + chronicle.getId() + "/character/description";
    }
}
