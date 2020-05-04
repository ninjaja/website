package com.company.website.controller.admin;

import com.company.website.model.Category;
import com.company.website.model.Subgroup;
import com.company.website.service.CategoryService;
import com.company.website.service.ProjectService;
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
 * @author Dmitry Matrizaev
 * @since 23.04.2020
 */
@Controller
public class SubgroupController {

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;

    public SubgroupController(CategoryService categoryService, SubgroupService subgroupService,
                              ProjectService projectService) {
        this.categoryService = categoryService;
        this.subgroupService = subgroupService;
        this.projectService = projectService;
    }

    @PostMapping("/admin/addSubgroup")
    public String addSubgroup(@Valid Subgroup subgroup, @RequestParam Integer categoryId, Model model) {
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        subgroup.setCategory(category);
        subgroupService.save(subgroup);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(category));
        return "redirect:/admin/" + category.getUrl();
    }

    @PostMapping("/admin/removeSubgroup")
    public String removeSubgroup(@RequestParam Integer categoryId, @RequestParam Integer subgroupId) {
        subgroupService.deleteById(subgroupId);
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        return "redirect:/admin/" + category.getUrl();
    }

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable String categoryUrl, @PathVariable String subgroupUrl, Model model) {
        Category category = categoryService.findByUrl(categoryUrl);
        Subgroup subgroup = subgroupService.findByUrl(subgroupUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return "admin/adminSubgroup";
    }

    @PostMapping("/editSubgroup")
    public String editSubgroup(@Valid Subgroup subgroup, @RequestParam Integer categoryId,
                               @RequestParam Integer subgroupId, Model model) {
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        subgroup.setId(subgroupId);
        subgroup.setCategory(category);
        Subgroup oldSubgroup = subgroupService.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        if (!subgroup.equals(oldSubgroup)) {
            subgroupService.save(subgroup);
            model.addAttribute("subgroup", subgroup);
            return "redirect:/" + category.getUrl() + "/" + subgroup.getUrl();
        }
        model.addAttribute("subgroup", oldSubgroup);
        return "redirect:/" + category.getUrl() + "/" + oldSubgroup.getUrl();
    }

}
