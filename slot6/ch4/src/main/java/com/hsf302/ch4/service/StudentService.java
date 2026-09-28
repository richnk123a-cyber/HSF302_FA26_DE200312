package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.List;

public interface StudentService {
    long count();                                                       // TODO 6
    Optional<Student> findById(Long id);                                // TODO 6
    List<Student> findAllOrderByGpaDesc();                              // TODO 7a
    Page<Student> findPage(int pageIndex, int size, String sortField);  // TODO 7b
    Optional<Student> findByStudentCode(String studentCode);
    boolean isEmailExisted(String email);
    long countActive();
    List<Student> searchByName(String keyword);
    List<Student> findByEmailDomain(String domain);
    List<Student> findWithoutEmail();
}

