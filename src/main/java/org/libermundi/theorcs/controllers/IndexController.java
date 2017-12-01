package org.libermundi.theorcs.controllers;

import org.libermundi.theorcs.domain.jpa.User;
import org.libermundi.theorcs.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"","/","index"})
    public String getIndexPage() {
        User user = userService.findByUsername("bob");
        return "index";
    }
}
