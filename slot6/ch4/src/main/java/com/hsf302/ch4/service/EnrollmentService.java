package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.EnrollmentView;
import com.hsf302.ch4.dto.StudentCreditDTO;
import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EnrollmentService {

    List<Course> getCoursesOfStudent(String studentCode);

    List<Student> getStudentsOfCourse(String courseCode);

    List<Student> findStudentsInCourse(String courseCode);

    long countStudentsInCourse(String courseCode);

    List<Student> findActiveStudentsInCourse(String courseCode);

    // ===== Exercise 2 - TODO 11 =====

    List<Student> findStudentsWithoutCourses();

    boolean isEnrolled(String studentCode, String courseCode);

    // ===== Exercise 2 - TODO 12 =====

    List<Student> findGoodStudentsInCourse(String courseCode, double minGpa);

    // ===== Exercise 2 - TODO 14 =====

    List<StudentCreditDTO> getCreditSummary(int minCredits);

    // ===== Exercise 2 - TODO 15 =====

    List<Student> findStudentsWithMoreThan(int n);

    // ===== Exercise 2 - TODO 16 =====

    Student getStudentWithCourses(String studentCode);

    // ===== Exercise 2 - TODO 18 =====

    List<EnrollmentView> getEnrollmentsOfDepartment(String deptCode);

    Page<Student> findStudentsInCoursePage(String courseCode, int pageIndex, int size);

    // ===== Bonus - TODO 25 =====

    List<Student> search(
            String courseCode,
            String semester,
            String deptCode,
            Double minGpa
    );

    // ===== Exercise 2 - TODO 20 =====

    void enroll(String studentCode, String courseCode);

    // ===== Exercise 2 - TODO 21 =====

    void unenroll(String studentCode, String courseCode);

    // ===== Exercise 2 - TODO 22 =====

    void switchCourse(String studentCode, String fromCode, String toCode);

    // ===== Exercise 2 - TODO 24 =====

    int removeEnrollmentsOfInactiveStudents();
}