package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.ProjectRepository;
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
 * @since 23.04.2020
 */

@Controller
public class SubgroupController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubgroupRepository subgroupRepository;

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/category/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable String categoryUrl, @PathVariable String subgroupUrl, Model model) {
        Category category = categoryRepository.findByUrl(categoryUrl);
        Subgroup subgroup = subgroupRepository.findByUrl(subgroupUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("projects", projectRepository.findAllBySubgroup(subgroup));
        return "subgroup";
    }

    @PostMapping("/addProject")
    public String add(@Valid Project project, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        project.setSubgroup(subgroup);
        projectRepository.save(project);
        model.addAttribute("projects", projectRepository.findAllBySubgroup(subgroup));
        return "redirect:/category/" + category.getUrl() + "/" + subgroup.getUrl();
    }

    @PostMapping("/editSubgroup")
    public String editSubgroup(@Valid Subgroup subgroup, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup oldSubgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        if (!subgroup.equals(oldSubgroup)) {
            subgroupRepository.save(subgroup);
            model.addAttribute("subgroup", subgroup);
            return "redirect:/category/" + category.getUrl() + "/" + subgroup.getUrl();
        }
        model.addAttribute("subgroup", oldSubgroup);
        return "redirect:/category/" + category.getUrl() + "/" + oldSubgroup.getUrl();
    }

}
