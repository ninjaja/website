package com.company.website.controller.admin;

import com.company.website.dto.CategoryDTO;
import com.company.website.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
public class AdminMainController {

    private static final String REDIRECT_TO_ADMIN = "redirect:/admin";

    private final CategoryService categoryService;

    public AdminMainController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin")
    public String greeting(final Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/adminHome";
    }

    @GetMapping("/admin/home")
    public String home() {
        return REDIRECT_TO_ADMIN;
    }

    @PostMapping("/admin/addCategory")
    public String addCategory(@Valid final CategoryDTO category, final Model model) {
        categoryService.save(category);
        model.addAttribute("categories", categoryService.findAll());
        return REDIRECT_TO_ADMIN;
    }

    @PostMapping("/admin/removeCategory")
    public String removeCategory(@RequestParam final String title) {
        categoryService.deleteByTitle(title);
        return REDIRECT_TO_ADMIN;
    }

}
