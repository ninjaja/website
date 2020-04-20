package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/")
    public String greeting() {
        return "home";
    }

    @GetMapping("/projects")
    public String allCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "projects";
    }

    @PostMapping("/projects")
    public String addCategory(@RequestParam String title, @RequestParam String url, @RequestParam String description, Model model) {
        Category category = new Category(title, url, description);
        categoryRepository.save(category);
        model.addAttribute("categories", categoryRepository.findAll());
        return "projects";
    }
}
