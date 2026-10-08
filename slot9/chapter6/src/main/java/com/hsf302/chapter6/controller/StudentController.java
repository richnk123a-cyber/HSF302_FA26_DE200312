package com.hsf302.chapter6.controller;

import com.hsf302.chapter6.dto.StudentForm;
import com.hsf302.chapter6.entity.Student;
import com.hsf302.chapter6.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final String FORM_VIEW = "students/form";

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ==================== COMMON MODEL ====================

    @ModelAttribute("majors")
    public List<String> majors() {
        return studentService.getMajors();
    }

    // ==================== READ ALL + SEARCH ====================

    @GetMapping
    public String list(
            @RequestParam(
                    value = "keyword",
                    required = false
            ) String keyword,
            Model model) {

        model.addAttribute(
                "students",
                studentService.search(keyword)
        );

        model.addAttribute("keyword", keyword);

        return "students/list";
    }

    // ==================== PAGINATION ====================

    @GetMapping(params = {"page", "size"})
    public String listPage(
            @RequestParam(
                    value = "page",
                    defaultValue = "0"
            ) int page,

            @RequestParam(
                    value = "size",
                    defaultValue = "5"
            ) int size,

            Model model) {

        // Không cho page âm
        if (page < 0) {
            page = 0;
        }

        // Không cho size không hợp lệ
        if (size <= 0) {
            size = 5;
        }

        Page<Student> studentPage =
                studentService.findPage(page, size);

        model.addAttribute(
                "students",
                studentPage.getContent()
        );

        model.addAttribute(
                "page",
                studentPage
        );

        return "students/list";
    }

    // ==================== SORTING ====================

    @GetMapping(params = "sort")
    public String listSorted(
            @RequestParam("sort") String sort,
            Model model) {

        String[] parts = sort.split(",", 2);

        String sortBy = parts[0].trim();

        String direction =
                parts.length > 1
                        ? parts[1].trim()
                        : "asc";

        model.addAttribute(
                "students",
                studentService.sort(
                        sortBy,
                        direction
                )
        );

        model.addAttribute(
                "sort",
                sortBy + "," + direction
        );

        return "students/list";
    }

    // ==================== READ ONE ====================

    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes ra) {

        return studentService.findById(id)
                .map(student -> {

                    model.addAttribute(
                            "student",
                            student
                    );

                    return "students/detail";
                })
                .orElseGet(() -> {

                    ra.addFlashAttribute(
                            "errorMsg",
                            "Không tìm thấy sinh viên ID: " + id
                    );

                    return "redirect:/students";
                });
    }

    // ==================== CREATE ====================

    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute(
                "student",
                new Student()
        );

        return formView(model, false);
    }

    @PostMapping("/create")
    public String create(
            @Valid
            @ModelAttribute("student")
            Student student,

            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        // Kiểm tra email trùng
        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(
                student.getEmail(),
                null)) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã tồn tại"
            );
        }

        // Có lỗi -> quay lại form
        if (bindingResult.hasErrors()) {
            return formView(model, false);
        }

        try {

            studentService.create(student);

        } catch (DataIntegrityViolationException e) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã tồn tại"
            );

            return formView(model, false);
        }

        ra.addFlashAttribute(
                "successMsg",
                "Thêm sinh viên thành công!"
        );

        return "redirect:/students";
    }

    // ==================== UPDATE ====================

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes ra) {

        return studentService.findById(id)
                .map(student -> {

                    model.addAttribute(
                            "student",
                            student
                    );

                    return formView(model, true);
                })
                .orElseGet(() -> {

                    ra.addFlashAttribute(
                            "errorMsg",
                            "Không tìm thấy sinh viên ID: " + id
                    );

                    return "redirect:/students";
                });
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable("id") Long id,

            @Valid
            @ModelAttribute("student")
            Student student,

            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        // Form không gửi id nên lấy id từ URL
        student.setId(id);

        // Kiểm tra email trùng với sinh viên khác
        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(
                student.getEmail(),
                id)) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã được sinh viên khác sử dụng"
            );
        }

        // Có lỗi validate
        if (bindingResult.hasErrors()) {
            return formView(model, true);
        }

        try {

            if (studentService.update(id, student)) {

                ra.addFlashAttribute(
                        "successMsg",
                        "Cập nhật thành công!"
                );

            } else {

                ra.addFlashAttribute(
                        "errorMsg",
                        "Không tìm thấy sinh viên ID: " + id
                );
            }

        } catch (DataIntegrityViolationException e) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã được sinh viên khác sử dụng"
            );

            return formView(model, true);
        }

        return "redirect:/students";
    }

    // ==================== DELETE ====================

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes ra) {

        if (studentService.delete(id)) {

            ra.addFlashAttribute(
                    "successMsg",
                    "Xóa sinh viên thành công!"
            );

        } else {

            ra.addFlashAttribute(
                    "errorMsg",
                    "Không tìm thấy sinh viên để xóa!"
            );
        }

        return "redirect:/students";
    }

    // ==================== FORM HELPER ====================

    private String formView(
            Model model,
            boolean isEdit) {

        model.addAttribute(
                "isEdit",
                isEdit
        );

        model.addAttribute(
                "pageTitle",
                isEdit
                        ? "Cập nhật sinh viên"
                        : "Thêm sinh viên mới"
        );

        return FORM_VIEW;
    }

    // ==================== DTO CREATE ====================

    @GetMapping("/dto/create")
    public String showDtoCreateForm(Model model) {

        model.addAttribute(
                "studentForm",
                new StudentForm()
        );

        model.addAttribute(
                "dtoEdit",
                false
        );

        model.addAttribute(
                "pageTitle",
                "Thêm sinh viên bằng DTO"
        );

        return "students/dto-form";
    }

    @PostMapping("/dto/create")
    public String createDto(
            @Valid
            @ModelAttribute("studentForm")
            StudentForm form,

            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(
                form.getEmail(),
                null)) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã tồn tại"
            );
        }

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "dtoEdit",
                    false
            );

            model.addAttribute(
                    "pageTitle",
                    "Thêm sinh viên bằng DTO"
            );

            return "students/dto-form";
        }

        try {

            studentService.createFromForm(form);

        } catch (DataIntegrityViolationException e) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã tồn tại"
            );

            model.addAttribute(
                    "dtoEdit",
                    false
            );

            model.addAttribute(
                    "pageTitle",
                    "Thêm sinh viên bằng DTO"
            );

            return "students/dto-form";
        }

        ra.addFlashAttribute(
                "successMsg",
                "Thêm sinh viên bằng DTO thành công!"
        );

        return "redirect:/students";
    }

    // ==================== DTO UPDATE ====================

    @GetMapping("/dto/{id}/edit")
    public String showDtoEditForm(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes ra) {

        StudentForm form =
                studentService.getStudentForm(id);

        if (form == null) {

            ra.addFlashAttribute(
                    "errorMsg",
                    "Không tìm thấy sinh viên ID: " + id
            );

            return "redirect:/students";
        }

        model.addAttribute(
                "studentForm",
                form
        );

        model.addAttribute(
                "dtoEdit",
                true
        );

        model.addAttribute(
                "pageTitle",
                "Cập nhật sinh viên bằng DTO"
        );

        return "students/dto-form";
    }

    @PostMapping("/dto/{id}/edit")
    public String updateDto(
            @PathVariable("id") Long id,

            @Valid
            @ModelAttribute("studentForm")
            StudentForm form,

            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        form.setId(id);

        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(
                form.getEmail(),
                id)) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã được sinh viên khác sử dụng"
            );
        }

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "dtoEdit",
                    true
            );

            model.addAttribute(
                    "pageTitle",
                    "Cập nhật sinh viên bằng DTO"
            );

            return "students/dto-form";
        }

        try {

            if (studentService.updateFromForm(id, form)) {

                ra.addFlashAttribute(
                        "successMsg",
                        "Cập nhật bằng DTO thành công!"
                );

            } else {

                ra.addFlashAttribute(
                        "errorMsg",
                        "Không tìm thấy sinh viên ID: " + id
                );
            }

        } catch (DataIntegrityViolationException e) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã được sinh viên khác sử dụng"
            );

            model.addAttribute(
                    "dtoEdit",
                    true
            );

            model.addAttribute(
                    "pageTitle",
                    "Cập nhật sinh viên bằng DTO"
            );

            return "students/dto-form";
        }

        return "redirect:/students";
    }
}