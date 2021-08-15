package com.company.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class TemplateController {

    @Autowired
    public AuthenticationManager authenticationManage;

    @GetMapping({"/hello"})
    public String getLoginView() {
        System.out.println(authenticationManage+"this is login");
        return "login";
    }

    @PutMapping("/courses")
     public String getCourses() {
     return "courses";
    }
}
