package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.model.Subgroup;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.SubgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    // TODO: 22.04.2020 изменить подход с @ModelAttribute

    private Category category;

    @GetMapping("/fromMainController")
    public String fromMainController(@ModelAttribute("categoryModel") Category category, Model model) {
        model.addAttribute("category", category);
        this.category = category;
        model.addAttribute("subgroups", subgroupRepository.getAllByCategory(category));
        return "category";
    }

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupRepository.getAllByCategory(category));
        return "category";
    }

    @PostMapping("/addSubgroup")
    public String add(@Valid Subgroup subgroup, Model model) {
        subgroup.setCategory(category);
        subgroupRepository.save(subgroup);
        model.addAttribute("subgroups", subgroupRepository.getAllByCategory(category));
        return "redirect:/category";
    }

}
