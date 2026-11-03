package vonguyenkhanh.example.datamodule.service;

import vonguyenkhanh.example.datamodule.pojo.Student;
import java.util.List;

public interface StudentService {
    Student create(Student s);
    Student findById(Long id);
    List<Student> findAll();
    Student update(Student s);
    void delete(Long id);
    List<Student> search(String keyword);
}
