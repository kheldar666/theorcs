package org.libermundi.theorcs.controllers.secure;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.ChronicleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChronicleController {

    private final ChronicleService chronicleService;

    public ChronicleController(ChronicleService chronicleService) {
        this.chronicleService = chronicleService;
    }

    @GetMapping("/secure/chronicle/create")
    public String create() {
        return "/secure/chronicle/create";
    }

    @GetMapping("/secure/chronicle/{chronicle}")
    @PreAuthorize("hasPermission(#chronicle, 'read')")
    public String home(Model model, @PathVariable Chronicle chronicle) {
        return "/secure/chronicle/home";
    }

}
