package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

@Controller
public class ProjectController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "home";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/adminconsole")
    public String allCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "adminconsole";
    }

    @PostMapping("/adminconsole")
    public String addCategory(@Valid Category category, Model model) {
        categoryRepository.save(category);
        model.addAttribute("categories", categoryRepository.findAll());
        return "redirect:/adminconsole";
    }
}
