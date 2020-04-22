package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.SubgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

@Controller
public class MainController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubgroupRepository subgroupRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "home";
    }

    @PostMapping("/addCategory")
    public String add(@Valid Category category, Model model) {
        categoryRepository.save(category);
        model.addAttribute("categories", categoryRepository.findAll());
        return "redirect:/home";
    }

    @PostMapping("/viewCategory")
    public String edit(@RequestParam(value = "name") String name, @ModelAttribute("categoryModel") Category category, Model model, RedirectAttributes redirectAttributes) {
        category = categoryRepository.findByTitle(name);
        redirectAttributes.addFlashAttribute("categoryModel", category);
        return "redirect:/fromMainController";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

}
