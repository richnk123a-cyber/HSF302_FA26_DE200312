package com.example.demo.repository;

import com.example.demo.model.KhoaHoc;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KhoaHocRepository extends JpaRepository<KhoaHoc, Long> {
    // WHERE upper(ten_khoa_hoc) LIKE upper('%keyword%') ORDER BY ...
    List<KhoaHoc> findByTenKhoaHocContainingIgnoreCase(String keyword, Sort sort);
}