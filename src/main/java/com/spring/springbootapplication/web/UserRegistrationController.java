package com.spring.springbootapplication.web;

import com.spring.springbootapplication.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("form", new UserRegistrationForm());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(
          @Valid @ModelAttribute("form") UserRegistrationForm form,
          BindingResult bindingResult,
          Model model) {

        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        try{
            userRegistrationService.register(form.getName(), form.getEmail(), form.getPassword());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "duplicate", e.getMessage());
            return "user/register";
        }
        model.addAttribute("name", form.getName());
        return "user/register_complete";
    }
        

}
