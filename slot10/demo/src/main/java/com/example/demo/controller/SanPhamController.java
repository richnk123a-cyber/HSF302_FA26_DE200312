package com.example.demo.controller;

import com.example.demo.model.SanPham;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("/sanpham")
public class SanPhamController {

    // Lưu tạm trong bộ nhớ: dùng chung cho mọi người dùng, mất khi restart.
    // CopyOnWriteArrayList an toàn khi nhiều request cùng thêm.
    private final List<SanPham> danhSach = new CopyOnWriteArrayList<>();

    // GET – hiển thị form rỗng
    @GetMapping("/them")
    public String showForm(Model model) {
        model.addAttribute("sanPham", new SanPham());     // tên "sanPham" = th:object
        return "sanpham/form";
    }

    // POST – nhận dữ liệu, lưu, rồi REDIRECT
    @PostMapping("/them")
    public String xuLyForm(@ModelAttribute("sanPham") SanPham sanPham, RedirectAttributes ra) {
        danhSach.add(sanPham);
        ra.addFlashAttribute("thongBao", "Thêm sản phẩm thành công!");   // sống qua 1 lần redirect
        return "redirect:/sanpham/ket-qua";
    }

    // GET – trang kết quả
    @GetMapping("/ket-qua")
    public String ketQua(Model model) {
        model.addAttribute("danhSach", danhSach);
        return "sanpham/ket-qua";
    }
}