package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.model.Subgroup;
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

    @GetMapping("/category/{categoryUrl}")
    public String viewCategory(@PathVariable String categoryUrl, Model model) {
        Category category = categoryRepository.findByUrl(categoryUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupRepository.findAllByCategory(category));
        return "category";
    }

    @PostMapping("/addSubgroup")
    public String add(@Valid Subgroup subgroup, @RequestParam Integer categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        subgroup.setCategory(category);
        subgroupRepository.save(subgroup);
        model.addAttribute("subgroups", subgroupRepository.findAllByCategory(category));
        return "redirect:/category/" + category.getUrl();
    }

    @PostMapping("/editCategory")
    public String editCategory(@Valid Category category, @RequestParam Integer id, Model model) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!category.equals(oldCategory)) {
            categoryRepository.save(category);
            model.addAttribute("category", category);
            return "redirect:/category/" + category.getUrl();
        }
        model.addAttribute("category", oldCategory);
        return "redirect:/category/" + oldCategory.getUrl();
    }

}
