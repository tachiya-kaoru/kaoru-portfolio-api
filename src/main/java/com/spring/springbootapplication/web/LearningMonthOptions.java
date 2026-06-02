package com.spring.springbootapplication.web;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public final class LearningMonthOptions {

    /** 今月 + 過去3ヶ月 = 4件 */
    public static final int RECENT_MONTH_COUNT = 3;

    private LearningMonthOptions() {
    }

    public static List<MonthOption> buildRecentMonths() {
        YearMonth current = YearMonth.now();
        List<MonthOption> options = new ArrayList<>();

        for (int i = 0; i < RECENT_MONTH_COUNT; i++) {
            options.add(MonthOption.from(current.minusMonths(i)));
        }

        return options;
    }

    public static MonthOption resolveSelectedMonth(String yearMonthValue) {
        if (yearMonthValue == null || yearMonthValue.isBlank()) {
            return MonthOption.from(YearMonth.now());
        }

        String[] parts = yearMonthValue.split("-");
        if (parts.length != 2) {
            return MonthOption.from(YearMonth.now());
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            YearMonth selected = YearMonth.of(year, month);

            if (!isSelectable(selected)) {
                return MonthOption.from(YearMonth.now());
            }

            return MonthOption.from(selected);
        } catch (NumberFormatException | java.time.DateTimeException ex) {
            return MonthOption.from(YearMonth.now());
        }
    }

    public static boolean isSelectable(YearMonth yearMonth) {
        YearMonth oldest = YearMonth.now().minusMonths(RECENT_MONTH_COUNT - 1);
        return !yearMonth.isAfter(YearMonth.now()) && !yearMonth.isBefore(oldest);
    }
}
