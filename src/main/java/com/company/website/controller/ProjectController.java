package com.company.website.controller;

import com.company.website.model.Category;
import com.company.website.model.Image;
import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.ImageRepository;
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
public class ProjectController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubgroupRepository subgroupRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/category/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewSubgroup(@PathVariable String categoryUrl, @PathVariable String subgroupUrl, @PathVariable String projectUrl, Model model) {
        Category category = categoryRepository.findByUrl(categoryUrl);
        Subgroup subgroup = subgroupRepository.findByUrl(subgroupUrl);
        Project project = projectRepository.findByUrl(projectUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("project", project);
        model.addAttribute("images", imageRepository.findAllByProject(project));
        return "project";
    }

    @PostMapping("/addImage")
    public String add(@Valid Image image, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, @RequestParam Integer projectId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        image.setProject(project);
        imageRepository.save(image);
        model.addAttribute("images", imageRepository.findAllByProject(project));
        return "redirect:/category/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
    }

    @PostMapping("/editProject")
    public String editProject(@Valid Project project, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, @RequestParam Integer projectId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        Project oldProject = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        if (!project.equals(oldProject)) {
            projectRepository.save(project);
            model.addAttribute("project", project);
            return "redirect:/category/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
        }
        model.addAttribute("project", oldProject);
        return "redirect:/category/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + oldProject.getUrl();
    }

}
