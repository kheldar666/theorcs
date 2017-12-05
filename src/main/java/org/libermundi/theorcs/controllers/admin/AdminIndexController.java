package org.libermundi.theorcs.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminIndexController {
    @GetMapping({"/admin","/admin/","/admin/index"})
    public String getIndexPage(){
        return "/admin/index";
    }
}
