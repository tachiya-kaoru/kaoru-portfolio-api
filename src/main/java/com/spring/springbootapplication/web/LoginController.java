package com.spring.springbootapplication.web;

import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("form", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("form") LoginForm form,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = userMapper.findByEmail(form.getEmail());

        if (user == null || !passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            redirectAttributes.addFlashAttribute("loginError", "メールアドレス、もしくはパスワードが間違っています");
            return "redirect:/login";
        }

        session.setAttribute("loginUserName", user.getName());
        return "redirect:/top";
    }
}