package com.spring.springbootapplication.web;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public class ProfileEditForm {
    @NotBlank(message = "自己紹介は必須項目です")
    @Size(min = 50, max = 200, message = "自己紹介は50文字以上200文字以下で入力してください")
    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}