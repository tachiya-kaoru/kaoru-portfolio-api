package com.spring.springbootapplication.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping("/top")
    public String showTop(Model model) {
        model.addAttribute("message", "簡易TOPページです");
        return "top";
    }
}