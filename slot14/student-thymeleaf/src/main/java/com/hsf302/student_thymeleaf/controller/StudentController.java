package com.hsf302.student_thymeleaf.controller;

import com.hsf302.student_thymeleaf.entity.Student;
import com.hsf302.student_thymeleaf.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Danh sách ngành dùng chung cho mọi request.
    @ModelAttribute("majors")
    public Map<String, String> majors() {
        Map<String, String> majors = new LinkedHashMap<>();

        majors.put("SE", "Kỹ thuật phần mềm");
        majors.put("AI", "Trí tuệ nhân tạo");
        majors.put("IA", "An toàn thông tin");
        majors.put("GD", "Thiết kế đồ họa");

        return majors;
    }

    // TODO 4: GET /students?keyword=...
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            Model model) {

        String normalizedKeyword =
                keyword == null ? "" : keyword.trim();

        List<Student> students =
                studentService.search(normalizedKeyword);

        model.addAttribute("students", students);
        model.addAttribute("keyword", normalizedKeyword);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute(
                "avgGpa",
                studentService.averageGpa(students)
        );
        model.addAttribute("activeMenu", "list");

        return "students/list";
    }

    // TODO 5: GET /students/{id}
    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra) {

        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("activeMenu", "list");

                    return "students/detail";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute(
                            "errorMessage",
                            "Không tìm thấy sinh viên có id = " + id
                    );

                    return "redirect:/students";
                });
    }

    // TODO 6: GET /students/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("student", new Student());
        model.addAttribute("activeMenu", "new");

        return "students/form";
    }

    // TODO 6: GET /students/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra) {

        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("activeMenu", "list");

                    return "students/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute(
                            "errorMessage",
                            "Không tìm thấy sinh viên có id = " + id
                    );

                    return "redirect:/students";
                });
    }
    // TODO 7: POST /students/save
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            Model model,
            RedirectAttributes ra) {
        // Kiểm tra mã sinh viên trùng.
        // Khi sửa, bỏ qua chính sinh viên đang được cập nhật.
        if (studentService.isStudentCodeDuplicated(
                student.getStudentCode(),
                student.getId())) {
            result.rejectValue(
                    "studentCode",
                    "duplicate",
                    "Mã sinh viên đã tồn tại"
            );
        }
        // Có lỗi thì trả về form, không redirect.
        if (result.hasErrors()) {
            model.addAttribute(
                    "activeMenu",
                    student.getId() == null ? "new" : "list"
            );
            return "students/form";
        }
        boolean isNew = student.getId() == null;
        Student savedStudent = studentService.save(student);
        String action = isNew ? "Thêm" : "Cập nhật";
        ra.addFlashAttribute(
                "successMessage",
                action + " sinh viên "
                        + savedStudent.getFullName()
                        + " thành công!"
        );
        return "redirect:/students";
    }
    // Xóa sinh viên.
    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes ra) {
        if (studentService.findById(id).isPresent()) {
            studentService.deleteById(id);
            ra.addFlashAttribute(
                    "successMessage",
                    "Đã xóa sinh viên có id = " + id
            );
        } else {
            ra.addFlashAttribute(
                    "errorMessage",
                    "Không tìm thấy sinh viên có id = " + id
            );
        }
        return "redirect:/students";
    }
}