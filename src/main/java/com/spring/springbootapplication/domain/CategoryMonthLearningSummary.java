package com.spring.springbootapplication.domain;

/**
 * チャート用：年月 × カテゴリごとの学習時間合計
 */
public class CategoryMonthLearningSummary {

    private Integer learningYear;
    private Integer learningMonth;
    private Long categoryId;
    private Integer totalMinutes;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }
}