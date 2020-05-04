package com.company.website.controller.admin;

import com.company.website.model.Category;
import com.company.website.service.CategoryService;
import com.company.website.service.SubgroupService;
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

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;

    public CategoryController(CategoryService categoryService, SubgroupService subgroupService) {
        this.categoryService = categoryService;
        this.subgroupService = subgroupService;
    }

    @PostMapping("/admin/addCategory")
    public String addCategory(@Valid Category category, Model model) {
        categoryService.save(category);
        model.addAttribute("categories", categoryService.findAll());
        return "redirect:/admin";
    }

    @PostMapping("/admin/removeCategory")
    public String removeCategory(@RequestParam Integer id) {
        categoryService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable String categoryUrl, Model model) {
        Category category = categoryService.findByUrl(categoryUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(category));
        return "admin/adminCategory";
    }

    @PostMapping("/admin/editCategory")
    public String editCategory(@Valid Category category, @RequestParam Integer id, Model model) {
        Category oldCategory = categoryService.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!category.equals(oldCategory)) {
            categoryService.save(category);
            model.addAttribute("category", category);
            return "redirect:/admin/" + category.getUrl();
        }
        model.addAttribute("category", oldCategory);
        return "redirect:/admin/" + oldCategory.getUrl();
    }

}
