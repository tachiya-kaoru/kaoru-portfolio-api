package com.spring.springbootapplication.domain;

import java.time.LocalDateTime;

public class LearningRecord {

    private Long id;
    private Long userId;
    private Long categoryId;
    private Integer learningYear;
    private Integer learningMonth;
    private String itemName;
    private Integer learningTimeMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLearningYear() {
        return learningYear;
    }

    public void setLearningYear(Integer learningYear) {
        this.learningYear = learningYear;
    }

    public Integer getLearningMonth() {
        return learningMonth;
    }

    public void setLearningMonth(Integer learningMonth) {
        this.learningMonth = learningMonth;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getLearningTimeMinutes() {
        return learningTimeMinutes;
    }

    public void setLearningTimeMinutes(Integer learningTimeMinutes) {
        this.learningTimeMinutes = learningTimeMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
