package com.spring.springbootapplication.web;

import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileEditController {

    private final UserMapper userMapper;

    public ProfileEditController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/profile/edit")
    public String showEditForm(Model model, HttpSession session) {
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

        ProfileEditForm form = new ProfileEditForm();
        form.setIntroduction(user.getIntroduction());

        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("form", form);

        return "profile/edit";
    }
}