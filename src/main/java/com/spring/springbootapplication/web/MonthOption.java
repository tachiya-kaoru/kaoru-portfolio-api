package com.spring.springbootapplication.web;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class MonthOption {

    private static final DateTimeFormatter LABEL_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy年M月");

    private final int year;
    private final int month;
    private final String value;
    private final String label;

    public MonthOption(int year, int month) {
        this.year = year;
        this.month = month;
        this.value = year + "-" + month;
        this.label = YearMonth.of(year, month).format(LABEL_FORMATTER);
    }

    public static MonthOption from(YearMonth yearMonth) {
        return new MonthOption(yearMonth.getYear(), yearMonth.getMonthValue());
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}