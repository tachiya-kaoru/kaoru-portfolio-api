package com.spring.springbootapplication.web;

import jakarta.validation.constraints.Size;

public class ProfileEditForm {

    @Size(min = 50, max = 200, message = "自己紹介は50文字以上200文字以下で入力してください")
    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}