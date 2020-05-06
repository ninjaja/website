package com.company.website.controller.admin;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.service.CategoryService;
import com.company.website.service.SubgroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 *
 * @author Dmitry Matrizaev
 * @since 22.04.2020
 */
@Controller
public class CategoryController {

    private static final String REDIRECT = "redirect:/admin/%s";

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;

    public CategoryController(CategoryService categoryService, SubgroupService subgroupService) {
        this.categoryService = categoryService;
        this.subgroupService = subgroupService;
    }

    @GetMapping("/admin/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable final String categoryUrl, final Model model) {
        final CategoryDTO category = categoryService.findByUrl(categoryUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(category));
        return "admin/adminCategory";
    }

    @PostMapping("/admin/editCategory")
    public String editCategory(@Valid final CategoryDTO category, final Model model) {
        categoryService.save(category);
        model.addAttribute("category", category);
        return String.format(REDIRECT, category.getUrl());
    }

    @PostMapping("/admin/addSubgroup")
    public String addSubgroup(@Valid final SubgroupDTO subgroup, @RequestParam final String categoryUrl,
                              final Model model) {
        final CategoryDTO category = categoryService.findByUrl(categoryUrl);
        subgroupService.save(subgroup, category);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(category));
        return String.format(REDIRECT, categoryUrl);
    }

    @PostMapping("/admin/removeSubgroup")
    public String removeSubgroup(@RequestParam final String categoryUrl, @RequestParam final String subgroupUrl) {
        subgroupService.deleteByUrl(subgroupUrl);
        return String.format(REDIRECT, categoryUrl);
    }

}
