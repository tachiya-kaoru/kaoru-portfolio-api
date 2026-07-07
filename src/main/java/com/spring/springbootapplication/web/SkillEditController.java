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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    @GetMapping("/skill/records/new")
    public String showNewForm(
            @RequestParam(name = "month") String month,
            @RequestParam(name = "categoryId") Long categoryId,
            Model model,
            HttpSession session) {
        Object loginUserEmail = session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
        }
        // ログイン確認（loginUserEmail が null → redirect:/login）

        MonthOption selectedMonth = LearningMonthOptions.resolveSelectedMonth(month);
        
        Category category = categoryMapper.findById(categoryId);

        if (category == null) {
            return "redirect:/skill/edit?month=" + selectedMonth.getValue();
        }

        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("category", category);
        model.addAttribute("form", new LearningRecordAddForm());

        return "skill/record/new";
    }

    @PostMapping("/skill/records/new")
        public String addRecord(
            @RequestParam(name = "month") String month,
            @RequestParam(name = "categoryId") Long categoryId,
            @Validated @ModelAttribute("form") LearningRecordAddForm form,
            BindingResult bindingResult,
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
        Category category = categoryMapper.findById(categoryId);
        if (category == null) {
            return "redirect:/skill/edit?month=" + selectedMonth.getValue();
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
            model.addAttribute("selectedMonth", selectedMonth);
            model.addAttribute("category", category);
            return "skill/record/new";
        }

        // ④ 重複チェック
        int duplicateCount = learningRecordMapper.countByUserIdAndYearAndMonthAndItemName(
            user.getId(),
            selectedMonth.getYear(),
            selectedMonth.getMonth(),
            form.getItemName());

        if (duplicateCount > 0) {
            bindingResult.rejectValue("itemName", "duplicate",
                    form.getItemName() + "は既に登録されています");
            model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
            model.addAttribute("selectedMonth", selectedMonth);
            model.addAttribute("category", category);
        return "skill/record/new";
        }

        LearningRecord record = new LearningRecord();
        record.setUserId(user.getId());
        record.setCategoryId(categoryId);
        record.setLearningYear(selectedMonth.getYear());
        record.setLearningMonth(selectedMonth.getMonth());
        record.setItemName(form.getItemName());
        record.setLearningTimeMinutes(Integer.parseInt(form.getLearningTimeMinutes()));
        learningRecordMapper.insert(record);
        model.addAttribute("registered", true);
        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("category", category);
        return "skill/record/new";
    }
    /* 学習時間を更新する */
    @PostMapping("/skill/records/update")
    public String updateRecord(
            @RequestParam(name = "recordId") Long recordId,
            @RequestParam(name = "month") String month,
            @RequestParam(name = "learningTimeMinutes") String learningTimeMinutes,
            Model model,
            HttpSession session) {

        // ① ログイン確認
        Object loginUserEmail = session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
        }
        User user = userMapper.findByEmail(loginUserEmail.toString());
        if (user == null) {
            return "redirect:/login";
        }

        MonthOption selectedMonth = LearningMonthOptions.resolveSelectedMonth(month);

        // ② 入力チェック
        if (learningTimeMinutes == null
                || learningTimeMinutes.isBlank()
                || !learningTimeMinutes.matches("^[0-9]+$")) {
            return "redirect:/skill/edit?month=" + selectedMonth.getValue();
        }

        int minutes = Integer.parseInt(learningTimeMinutes);

        // ③ DB更新（自分のレコードだけ）
        int updated = learningRecordMapper.updateLearningTimeMinutes(
                recordId,
                user.getId(),
                minutes);

        if (updated == 0) {
            // レコードが見つからない or 他人のデータ
            return "redirect:/skill/edit?month=" + selectedMonth.getValue();
        }

        // ④ 編集画面のデータを再取得
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
        model.addAttribute("updateCompleted", true);
        /* 更新した学習時間を表示 */
        model.addAttribute("updatedMinutes", minutes);
        for (LearningRecord record : learningRecords) {
            if (record.getId().equals(recordId)) {
                model.addAttribute("updatedItemName", record.getItemName());
                break;
            }
        }

        // ⑤ 編集画面を表示
        return "skill/edit";
    }
     /* 学習項目を削除する */
    @PostMapping("/skill/records/delete")
    public String deleteRecord(
        @RequestParam(name = "recordId") Long recordId,
        @RequestParam(name = "month") String month,
        Model model,
        HttpSession session) {
    
        // ① ログイン確認
        Object loginUserEmail = session.getAttribute("loginUserEmail");
        if (loginUserEmail == null) {
            return "redirect:/login";
            }
        User user = userMapper.findByEmail(loginUserEmail.toString());
        if (user == null) {
            return "redirect:/login";
        }
    
        MonthOption selectedMonth = LearningMonthOptions.resolveSelectedMonth(month);
    
        // ② 削除前に項目名を取得
        List<LearningRecord> learningRecords = learningRecordMapper.findByUserIdAndYearAndMonth(
                user.getId(),
                selectedMonth.getYear(),
                selectedMonth.getMonth());
    
        String deletedItemName = null;
        for (LearningRecord record : learningRecords) {
            if (record.getId().equals(recordId)) {
                deletedItemName = record.getItemName();
                break;
            }
        }
    
        if (deletedItemName == null) {
                return "redirect:/skill/edit?month=" + selectedMonth.getValue();
        }
    
        // ③ DB削除（自分のレコードだけ）
        int deleted = learningRecordMapper.deleteByIdAndUserId(
                recordId,
                user.getId());
    
        if (deleted == 0) {
                return "redirect:/skill/edit?month=" + selectedMonth.getValue();
        }
    
        // ④ 編集画面のデータを再取得
        List<MonthOption> monthOptions = LearningMonthOptions.buildRecentMonths();
        List<Category> categories = categoryMapper.findAll();
        learningRecords = learningRecordMapper.findByUserIdAndYearAndMonth(
                user.getId(),
                selectedMonth.getYear(),
                selectedMonth.getMonth());
    
        model.addAttribute("headerNav", HeaderNavMode.LOGOUT);
        model.addAttribute("monthOptions", monthOptions);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("categories", categories);
        model.addAttribute("learningRecords", learningRecords);
        model.addAttribute("deleteCompleted", true);
        model.addAttribute("deletedItemName", deletedItemName);
    
        // ⑤ 編集画面を表示
        return "skill/edit";
    }
}
