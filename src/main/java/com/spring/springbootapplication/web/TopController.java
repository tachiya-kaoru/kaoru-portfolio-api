package com.spring.springbootapplication.web;

import com.spring.springbootapplication.domain.Category;
import com.spring.springbootapplication.domain.CategoryMonthLearningSummary;
import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.CategoryMapper;
import com.spring.springbootapplication.mapper.LearningRecordMapper;
import com.spring.springbootapplication.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    private final UserMapper userMapper;
    private final LearningRecordMapper learningRecordMapper;
    private final CategoryMapper categoryMapper;

    public TopController(
            UserMapper userMapper,
            LearningRecordMapper learningRecordMapper,
            CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.learningRecordMapper = learningRecordMapper;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/top")
    public String showTop(Model model, HttpSession session) {
        Object loginUserName = session.getAttribute("loginUserName");
        if (loginUserName == null) {
            return "redirect:/login";
        }
        Object loginUserEmail = session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
        }

        User user = userMapper.findByEmail(loginUserEmail.toString());
        if (user == null) {
            return "redirect:/login";
        }

        // --- チャート用：直近3ヶ月（先々月〜今月）---
        YearMonth thisMonth = YearMonth.now();
        YearMonth twoMonthsAgo = thisMonth.minusMonths(2);

        List<CategoryMonthLearningSummary> summaries =
                learningRecordMapper.sumMinutesByUserIdAndMonthRange(
                        user.getId(),
                        twoMonthsAgo.getYear(),
                        twoMonthsAgo.getMonthValue(),
                        thisMonth.getYear(),
                        thisMonth.getMonthValue());

        // 「年-月-カテゴリID」→ 合計分数 の辞書を作る
        Map<String, Integer> minutesMap = new HashMap<>();
        for (CategoryMonthLearningSummary summary : summaries) {
            String key = summary.getLearningYear()
                    + "-" + summary.getLearningMonth()
                    + "-" + summary.getCategoryId();
            int minutes = summary.getTotalMinutes() != null
                    ? summary.getTotalMinutes()
                    : 0;
            minutesMap.put(key, minutes);
        }

        List<YearMonth> chartMonths = List.of(
                thisMonth.minusMonths(2),
                thisMonth.minusMonths(1),
                thisMonth);
        List<String> chartMonthLabels = List.of("先々月", "先月", "今月");

        // カテゴリごとに、3ヶ月分の分数リストを作る
        List<Category> categories = categoryMapper.findAll();
        List<String> chartCategoryNames = new ArrayList<>();
        List<List<Integer>> chartSeriesData = new ArrayList<>();

        for (Category category : categories) {
            chartCategoryNames.add(category.getCategoryName());
            List<Integer> data = new ArrayList<>();
            for (YearMonth yearMonth : chartMonths) {
                String key = yearMonth.getYear()
                        + "-" + yearMonth.getMonthValue()
                        + "-" + category.getId();
                data.add(minutesMap.getOrDefault(key, 0));
            }
            chartSeriesData.add(data);
        }

        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("user", user);
        model.addAttribute("chartMonthLabels", chartMonthLabels);
        model.addAttribute("chartCategoryNames", chartCategoryNames);
        model.addAttribute("chartSeriesData", chartSeriesData);
        return "top";
    }
}