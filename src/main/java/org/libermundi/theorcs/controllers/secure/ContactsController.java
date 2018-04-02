package org.libermundi.theorcs.controllers.secure;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.domain.jpa.messaging.AddressBook;
import org.libermundi.theorcs.domain.jpa.messaging.Message;
import org.libermundi.theorcs.domain.jpa.messaging.MessageFolder;
import org.libermundi.theorcs.forms.MessageForm;
import org.libermundi.theorcs.services.chronicle.CharacterService;
import org.libermundi.theorcs.services.messaging.AddressBookService;
import org.libermundi.theorcs.services.messaging.MessagingService;
import org.libermundi.theorcs.services.security.SecurityService;
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
import java.util.Locale;

@Slf4j
@Controller
public class ContactsController {

    private final CharacterService characterService;

    private final SecurityService securityService;

    private final MessagingService messagingService;

    private final MessageSource messageSource;

    private final AddressBookService addressBookService;

    public ContactsController(CharacterService characterService, SecurityService securityService,
                              MessagingService messagingService, MessageSource messageSource, AddressBookService addressBookService) {
        this.characterService = characterService;
        this.securityService = securityService;
        this.messagingService = messagingService;
        this.messageSource = messageSource;
        this.addressBookService = addressBookService;
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/contacts")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String defaultContacts(@PathVariable Chronicle chronicle,
                           HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");
        AddressBook defaultAddressBook = addressBookService.findDefaultAddressBook(currentCharacter);

        return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/contacts/" + defaultAddressBook.getId();
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/contacts/{addressBook}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String contacts(Model model, @PathVariable Chronicle chronicle,
                           @PathVariable AddressBook addressBook,
                           HttpSession session) {
        Character currentCharacter = (Character)session.getAttribute("_currentCharacter");

        if(addressBook.getOwner().equals(currentCharacter)){
            model.addAttribute("currentAddressBook",addressBook);
            model.addAttribute("addressBooks",addressBookService.findAllByOwner(currentCharacter));
            return "/secure/chronicle/messaging/contacts";
        } else {
            return "redirect:/secure/chronicle/" + chronicle.getId() + "/messaging/contacts";
        }
    }

    @GetMapping("/secure/chronicle/{chronicle}/messaging/contacts/add")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String addContact(@PathVariable Chronicle chronicle){
        return "/layout/components/contacts :: addContactForm";
    }
}
