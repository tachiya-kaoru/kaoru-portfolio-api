package com.spring.springbootapplication.web;

import com.spring.springbootapplication.domain.Category;
import com.spring.springbootapplication.domain.LearningRecord;
import com.spring.springbootapplication.domain.User;
import com.spring.springbootapplication.mapper.CategoryMapper;
import com.spring.springbootapplication.mapper.LearningRecordMapper;
import com.spring.springbootapplication.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SkillEditController {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final LearningRecordMapper learningRecordMapper;

    public SkillEditController(
            UserMapper userMapper,
            CategoryMapper categoryMapper,
            LearningRecordMapper learningRecordMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.learningRecordMapper = learningRecordMapper;
    }

    @GetMapping("/skill/edit")
    public String showEditForm(
            @RequestParam(name = "month", required = false) String month,
            Model model,
            HttpSession session) {
        Object loginUserEmail = session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
        }

        User user = userMapper.findByEmail(loginUserEmail.toString());
        if (user == null) {
            return "redirect:/login";
        }

        MonthOption selectedMonth = LearningMonthOptions.resolveSelectedMonth(month);
        List<MonthOption> monthOptions = LearningMonthOptions.buildRecentMonths();
        List<Category> categories = categoryMapper.findAll();
        List<LearningRecord> learningRecords = learningRecordMapper.findByUserIdAndYearAndMonth(
                user.getId(),
                selectedMonth.getYear(),
                selectedMonth.getMonth());

        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("monthOptions", monthOptions);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("categories", categories);
        model.addAttribute("learningRecords", learningRecords);

        return "skill/edit";
    }
}