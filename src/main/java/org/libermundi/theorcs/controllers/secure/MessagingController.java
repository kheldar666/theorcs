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
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
public class MessagingController {

    private final CharacterService characterService;

    private final SecurityService securityService;

    private final MessagingService messagingService;

    private final MessageSource messageSource;

    public MessagingController(CharacterService characterService, SecurityService securityService,
                               MessagingService messagingService, MessageSource messageSource) {
        this.characterService = characterService;
        this.securityService = securityService;
        this.messagingService = messagingService;
        this.messageSource = messageSource;
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
    public String folders(Model model, @PathVariable Chronicle chronicle, @PathVariable MessageFolder folder, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        model.addAttribute("messageFolder", folder);
        model.addAttribute("messageList", messagingService.findMessagesByFolder(currentCharacter, folder));
        model.addAttribute("folderList", messagingService.getFolderList(currentCharacter));

        return "/secure/chronicle/messaging/folders";
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders/{folder}/read/{message}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String readMessage(Model model, @PathVariable Chronicle chronicle, @PathVariable MessageFolder messageFolder, @PathVariable Message message, HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        if(messagingService.isRecipent(message, currentCharacter) || messagingService.isSender(message,currentCharacter)){
            model.addAttribute("folderList", messagingService.getFolderList(currentCharacter));

            model.addAttribute("message", message);

            model.addAttribute("messageFolder", messageFolder);

            messagingService.markAsRead(message);

            return "/secure/chronicle/messaging/read";
        } else {
            return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders";
        }
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders/{folder}/reply/{message}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String replyMessage(Model model, @PathVariable Chronicle chronicle, @PathVariable MessageFolder messageFolder,
                               @PathVariable Message message,
                               @RequestParam(defaultValue = "single") String mode,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        if(messagingService.isRecipent(message, currentCharacter) || messagingService.isSender(message,currentCharacter)){

            MessageForm replyMessage = new MessageForm();

            // Let's prepare the Data for the Form
            String subject;
            String content;
            switch(mode.toLowerCase()){
                case "single":
                    subject=messageSource.getMessage("components.messaging.write.form.reply",new String[]{message.getContent().getSubject()},locale);
                    content=messageSource.getMessage("components.messaging.write.form.reply_body",new String[]{message.getContent().getContent()},locale);
                    replyMessage.getTo().add(message.getSender());
                    break;

                case "forward":
                    subject=messageSource.getMessage("components.messaging.write.form.forward",new String[]{message.getContent().getSubject()},locale);
                    content=messageSource.getMessage("components.messaging.write.form.forward_body",new String[]{message.getContent().getContent()},locale);
                    break;

                default: //Reply All
                    subject=messageSource.getMessage("components.messaging.write.form.reply",new String[]{message.getContent().getSubject()},locale);
                    content=messageSource.getMessage("components.messaging.write.form.reply_body",new String[]{message.getContent().getContent()},locale);
                    replyMessage.getTo().add(message.getSender());
                    replyMessage.getTo().addAll(message.getToRecipient());
                    replyMessage.getTo().remove(currentCharacter);
                    replyMessage.getCc().addAll(message.getCcRecipient());
                    break;
            }

            replyMessage.setSubject(subject);
            replyMessage.setContent(content);

            redirectAttributes.addFlashAttribute("newMessage", replyMessage);
            return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/write";
        } else {
            return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders";
        }
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders/{folder}/trash/{message}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String moveMessageToTrash(Model model, @PathVariable Chronicle chronicle, @PathVariable Long folder,
                                @PathVariable Message message,
                                HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        //Check if the currentCharacter is allow to access this message
        if(messagingService.isRecipent(message, currentCharacter) || messagingService.isSender(message,currentCharacter)){

            //Check if the Message is stored in a folder that belongs to the currentCharacter
            MessageFolder messageFolder = messagingService.findMessageFolderById(currentCharacter, folder);

            if(messageFolder != null) {
                messagingService.moveToTrash(message);
            }

        }
        return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders/" + folder;
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders/{folder}/restore/{message}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String restoreMessage(Model model, @PathVariable Chronicle chronicle, @PathVariable MessageFolder folder,
                                     @PathVariable Message message,
                                     HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        //Check if the currentCharacter is allow to access this message
        if(messagingService.isRecipent(message, currentCharacter) || messagingService.isSender(message,currentCharacter)){

            //Check if the Message is stored in a folder that belongs to the currentCharacter
            if(folder.getOwner().equals(currentCharacter)) {
                if(message.getSender()==null || !message.getSender().equals(currentCharacter)){
                    messagingService.moveToInbox(message);
                } else {
                    messagingService.moveToSent(message);
                }
            }
        }
        return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders/" + folder;
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/folders/{folder}/delete/{message}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String deleteMessage(Model model, @PathVariable Chronicle chronicle, @PathVariable Long folder,
                               @PathVariable Message message,
                               HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        //Check if the currentCharacter is allow to access this message
        if(messagingService.isRecipent(message, currentCharacter) || message.getSender().equals(currentCharacter)){

            //Check if the Message is stored in a folder that belongs to the currentCharacter
            MessageFolder messageFolder = messagingService.findMessageFolderById(currentCharacter, folder);

            if(messageFolder != null) {
                messagingService.delete(message);
            }

        }
        return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/folders/" + folder;
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/write")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String write(Model model, @PathVariable Chronicle chronicle,
            @RequestParam(defaultValue = "false") Boolean multiTo,
            @RequestParam(defaultValue = "false") Boolean showCcBcc
                ) {
        model.addAttribute("multiTo", multiTo);
        model.addAttribute("showCcBcc", showCcBcc);
        if(!model.containsAttribute("newMessage")) { // Can happen when there is a Reply/Foward
            model.addAttribute("newMessage", new MessageForm());
        }
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

    @GetMapping("/secure/chronicle/{chronicle}/messaging/contacts")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String contacts(Model model, @PathVariable Chronicle chronicle) {
        return "/secure/chronicle/messaging/contacts";
    }

}
