package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    long count();

    List<Course> findAllOrderByCode();

    Optional<Course> findById(Long id);

    Optional<Course> findByCode(String code);

    List<Course> findBySemester(String semester);

    long countBySemester(String semester);

    // ===== Exercise 2 - TODO 10 =====

    List<Course> findCoursesOfStudent(String studentCode);

    List<Course> findCoursesOfDepartment(String deptCode, boolean distinct);

    // ===== Exercise 2 - TODO 11 =====

    List<Course> findCoursesWithoutStudents();
}