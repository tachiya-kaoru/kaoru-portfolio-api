package com.spring.springbootapplication.web;

import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    private final UserMapper userMapper;

    public TopController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/top")
    public String showTop(Model model, HttpSession session) {
        Object loginUserName = session.getAttribute("loginUserName");
        if (loginUserName == null) {
            return "redirect:/login";
        }
        Object loginUserEmail = session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
        }

        User user = userMapper.findByEmail(loginUserEmail.toString());
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("user", user);
        return "top";
    }
}