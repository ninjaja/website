package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.SubgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

/**
 *
 * @author Dmitry Matrizaev
 * @since 22.04.2020
 */

@Controller
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubgroupRepository subgroupRepository;

    @PostMapping("/addCategory")
    public String addCategory(@Valid Category category, Model model) {
        categoryRepository.save(category);
        model.addAttribute("categories", categoryRepository.findAll());
        return "redirect:/";
    }

    @PostMapping("/removeCategory")
    public String removeCategory(@RequestParam Integer id) {
        categoryRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable String categoryUrl, Model model) {
        Category category = categoryRepository.findByUrl(categoryUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupRepository.findAllByCategory(category));
        return "category";
    }

    @PostMapping("/editCategory")
    public String editCategory(@Valid Category category, @RequestParam Integer id, Model model) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!category.equals(oldCategory)) {
            categoryRepository.save(category);
            model.addAttribute("category", category);
            return "redirect:/" + category.getUrl();
        }
        model.addAttribute("category", oldCategory);
        return "redirect:/" + oldCategory.getUrl();
    }




}
