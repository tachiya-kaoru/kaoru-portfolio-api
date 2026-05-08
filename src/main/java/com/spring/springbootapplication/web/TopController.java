package com.spring.springbootapplication.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class TopController {

    @GetMapping("/top")
    public String showTop(Model model, HttpSession session) {
        Object loginUserName = session.getAttribute("loginUserName");
        if (loginUserName == null) {
            return "redirect:/register";
        }
        model.addAttribute("loginUserName", loginUserName);    
        model.addAttribute("message", "簡易TOPページです");
        return "top";
    }
}