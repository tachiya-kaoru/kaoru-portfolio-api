package com.spring.springbootapplication.web;

import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ProfileEditController {

    private final UserMapper userMapper;
    private final HttpSession session;

    public ProfileEditController(UserMapper userMapper, HttpSession session) {
        this.userMapper = userMapper;
        this.session = session;
    }

    @GetMapping("/profile/edit")
    public String showEditForm(Model model) {
        String loginUserEmail = (String) session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
        }

        User user = userMapper.findByEmail(loginUserEmail);
        if (user == null) {
            return "redirect:/login";
        }

        ProfileEditForm form = new ProfileEditForm();
        form.setIntroduction(user.getIntroduction());

        model.addAttribute("form", form);
        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        return "profile/edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @Validated @ModelAttribute("form") ProfileEditForm form,
            BindingResult bindingResult,
            @RequestParam(value = "avatarImage", required = false) MultipartFile avatarImage,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
            return "profile/edit";
        }

        String loginUserEmail = (String) session.getAttribute("loginUserEmail");
        User user = userMapper.findByEmail(loginUserEmail);

        user.setIntroduction(form.getIntroduction());

        if (avatarImage != null && !avatarImage.isEmpty()) {
            try {
                String uploadsDir = "build/resources/main/static/uploads/";
                Files.createDirectories(Paths.get(uploadsDir));
                String fileName = System.currentTimeMillis() + "_" + avatarImage.getOriginalFilename();
                Path savePath = Paths.get(uploadsDir + fileName);
                Files.write(savePath, avatarImage.getBytes());
                user.setAvatarImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
                return "profile/edit";
            }
        }

        userMapper.update(user);

        return "redirect:/top";
    }
}