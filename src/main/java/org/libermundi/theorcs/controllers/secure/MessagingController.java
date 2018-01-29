package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.forms.MessageForm;
import org.libermundi.theorcs.services.CharacterService;
import org.libermundi.theorcs.services.MessagingService;
import org.libermundi.theorcs.services.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class MessagingController {

    private final CharacterService characterService;

    private final SecurityService securityService;

    private final MessagingService messagingService;

    public MessagingController(CharacterService characterService, SecurityService securityService, MessagingService messagingService) {
        this.characterService = characterService;
        this.securityService = securityService;
        this.messagingService = messagingService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String defaultFolder(@PathVariable Chronicle chronicle, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        MessageFolder messageFolder = messagingService.findInbox(currentCharacter);

        return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders/" + messageFolder.getId();
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders/{folder}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String folders(Model model, @PathVariable Chronicle chronicle, @PathVariable Long folder, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        MessageFolder messageFolder = messagingService.findMessageFolderById(currentCharacter, folder);

        model.addAttribute("messageList", messagingService.findMessagesByFolder(currentCharacter, messageFolder));
        model.addAttribute("folderList", messagingService.getFolderList(currentCharacter));

        return "/secure/chronicle/messaging/folders";
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/write")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String write(Model model, @PathVariable Chronicle chronicle) {
        model.addAttribute("newMessage", new MessageForm());
        model.addAttribute("characterList", characterService.getAllCharacters(securityService.getCurrentUser(),chronicle));
        model.addAttribute("contactList", characterService.getAllContacts(securityService.getCurrentUser(),chronicle));
        return "/secure/chronicle/messaging/write";
    }

    @PostMapping("/secure/chronicle/{chronicle}/messaging/send")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String send(Model model, @PathVariable Chronicle chronicle,
                       @Valid @ModelAttribute("newMessage") MessageForm messageForm, BindingResult bindingResult) {


        if(messageForm.getTo().isEmpty() &&
                messageForm.getCc().isEmpty() &&
                messageForm.getBcc().isEmpty()) {
            ObjectError error = new ObjectError("to","You need to select at least one contact in the 'to','cc' or 'bcc' field");
            bindingResult.addError(error);
        }

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "/secure/chronicle/messaging/write";
        }

        messagingService.sendMessage(messageForm);

        return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders";
    }

}
