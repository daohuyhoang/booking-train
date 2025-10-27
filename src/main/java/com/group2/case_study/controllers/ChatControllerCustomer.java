package com.group2.case_study.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatControllerCustomer {

    @GetMapping("/chat")
    public String chatCustomer() {
        return "flight/index";
    }
}
