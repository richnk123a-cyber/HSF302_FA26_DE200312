package com.hsf302.chapter6.service.impl;

import com.hsf302.chapter6.dto.StudentForm;
import com.hsf302.chapter6.entity.Student;
import com.hsf302.chapter6.repository.StudentRepository;
import com.hsf302.chapter6.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ==================== READ ALL ====================

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    // ==================== CREATE ====================

    @Override
    @Transactional
    public Student create(Student student) {
        // Create luôn phải tạo bản ghi mới
        student.setId(null);

        return studentRepository.save(student);
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public boolean update(Long id, Student data) {

        return studentRepository.findById(id)
                .map(existing -> {

                    existing.setName(data.getName());
                    existing.setEmail(data.getEmail());
                    existing.setAge(data.getAge());
                    existing.setMajor(data.getMajor());
                    existing.setGpa(data.getGpa());

                    /*
                     * Không cần gọi save().
                     * existing đang là entity managed trong transaction.
                     * Hibernate sẽ tự UPDATE khi transaction commit
                     * nhờ cơ chế dirty checking.
                     */
                    return true;
                })
                .orElse(false);
    }

    // ==================== DELETE ====================

    @Override
    @Transactional
    public boolean delete(Long id) {

        if (!studentRepository.existsById(id)) {
            return false;
        }

        studentRepository.deleteById(id);

        return true;
    }

    // ==================== EMAIL CHECK ====================

    @Override
    public boolean isEmailTaken(String email, Long excludeId) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String value = email.trim();

        // Create
        if (excludeId == null) {
            return studentRepository.existsByEmailIgnoreCase(value);
        }

        // Update:
        // kiểm tra email có thuộc sinh viên KHÁC hay không
        return studentRepository.existsByEmailIgnoreCaseAndIdNot(
                value,
                excludeId
        );
    }

    // ==================== MAJORS ====================

    @Override
    public List<String> getMajors() {
        return List.of(
                "CNTT",
                "KTPM",
                "HTTT",
                "ATTT",
                "MMT"
        );
    }

    // ==================== SEARCH ====================

    @Override
    public List<Student> search(String keyword) {

        // Không nhập keyword
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }

        String value = keyword.trim();

        return studentRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        value,
                        value,
                        Sort.by(Sort.Direction.ASC, "id")
                );
    }

    // ==================== PAGINATION ====================

    @Override
    public Page<Student> findPage(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "id")
        );

        return studentRepository.findAll(pageable);
    }

    // ==================== SORTING ====================

    @Override
    public List<Student> sort(String sortBy, String direction) {

        /*
         * Chỉ cho phép sort những field thực sự tồn tại
         * trong entity Student.
         */
        List<String> allowedFields = List.of(
                "id",
                "name",
                "email",
                "age",
                "major",
                "gpa"
        );

        /*
         * Nếu URL truyền field không hợp lệ:
         * /students?sort=abc,asc
         *
         * thì mặc định sort theo id.
         */
        if (sortBy == null || !allowedFields.contains(sortBy)) {
            sortBy = "id";
        }

        /*
         * Chỉ chấp nhận ASC / DESC.
         * Mọi giá trị khác đều mặc định ASC.
         */
        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return studentRepository.findAll(
                Sort.by(sortDirection, sortBy)
        );
    }

    // ==================== DTO ====================

    @Override
    public StudentForm getStudentForm(Long id) {

        return studentRepository.findById(id)
                .map(this::mapToForm)
                .orElse(null);
    }

    @Override
    @Transactional
    public Student createFromForm(StudentForm form) {

        Student student = mapToEntity(form);

        // DTO Create luôn phải INSERT
        student.setId(null);

        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public boolean updateFromForm(
            Long id,
            StudentForm form) {

        return studentRepository.findById(id)
                .map(existing -> {

                    existing.setName(form.getName());
                    existing.setEmail(form.getEmail());
                    existing.setAge(form.getAge());
                    existing.setMajor(form.getMajor());
                    existing.setGpa(form.getGpa());

                    return true;
                })
                .orElse(false);
    }

    // ==================== DTO MAPPING ====================

    private StudentForm mapToForm(Student student) {

        StudentForm form = new StudentForm();

        form.setId(student.getId());
        form.setName(student.getName());
        form.setEmail(student.getEmail());
        form.setAge(student.getAge());
        form.setMajor(student.getMajor());
        form.setGpa(student.getGpa());

        return form;
    }

    private Student mapToEntity(StudentForm form) {

        Student student = new Student();

        student.setName(form.getName());
        student.setEmail(form.getEmail());
        student.setAge(form.getAge());
        student.setMajor(form.getMajor());
        student.setGpa(form.getGpa());

        return student;
    }
}