package com.company.website.controller.admin;

import com.company.website.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
public class AdminMainController {

    private final CategoryService categoryService;

    public AdminMainController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin")
    public String greeting(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/adminHome";
    }

    @GetMapping("/admin/home")
    public String home() {
        return "redirect:/admin";
    }

}
