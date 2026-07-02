package com.spring.springbootapplication.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LearningRecordAddForm {

    @NotBlank(message = "項目名は必ず入力してください")
    @Size(max = 50, message = "項目名は50文字以内で入力してください")
    private String itemName;

    @NotBlank(message = "学習時間は必ず入力してください")
    @Pattern(
        regexp = "^[0-9]+$",
        message = "学習時間は0以上の数字で入力してください"
    )
    private String learningTimeMinutes;

    
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLearningTimeMinutes() {
        return learningTimeMinutes;
    }

    public void setLearningTimeMinutes(String learningTimeMinutes) {
        this.learningTimeMinutes = learningTimeMinutes;
    }
    
}
