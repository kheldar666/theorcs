package org.libermundi.theorcs.controllers.secure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecureIndexController {
    @GetMapping(value={"/secure/","/secure/index"})
    public String getIndexPage() {
        return "/secure/index";
    }
}
