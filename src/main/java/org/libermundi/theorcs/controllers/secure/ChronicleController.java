package org.libermundi.theorcs.controllers.secure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChronicleController {

    @GetMapping("/secure/chronicle/create")
    public String create() {
        return "/secure/chronicle/create";
    }
}
