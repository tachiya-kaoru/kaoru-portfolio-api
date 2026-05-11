package com.spring.springbootapplication.web;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {


    @NotBlank(message = "メールアドレスは必ず入力してください")
    private String email;

    @NotBlank(message = "パスワードは必ず入力してください")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
