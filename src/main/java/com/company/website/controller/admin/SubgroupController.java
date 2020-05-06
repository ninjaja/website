package com.company.website.controller.admin;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.service.CategoryService;
import com.company.website.service.ImageService;
import com.company.website.service.ProjectService;
import com.company.website.service.SubgroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author Dmitry Matrizaev
 * @since 23.04.2020
 */
@Controller
public class SubgroupController {

    private static final String REDIRECT_SUBGROUP = "redirect:/admin/%s/%s";

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;
    private final ImageService imageService;

    public SubgroupController(CategoryService categoryService, SubgroupService subgroupService,
                              ProjectService projectService, ImageService imageService) {
        this.categoryService = categoryService;
        this.subgroupService = subgroupService;
        this.projectService = projectService;
        this.imageService = imageService;
    }

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                               final Model model) {
        final CategoryDTO category = categoryService.findByUrl(categoryUrl);
        final SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return "admin/adminSubgroup";
    }

    @PostMapping("/admin/editSubgroup")
    public String editSubgroup(@Valid final SubgroupDTO subgroup, @RequestParam final String categoryUrl,
                               final Model model) {
        final CategoryDTO category = categoryService.findByUrl(categoryUrl);
        subgroupService.save(subgroup, category);
        model.addAttribute("subgroup", subgroup);
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroup.getUrl());
    }

    @PostMapping("/admin/addProject")
    public String addProject(@Valid final ProjectDTO project, @RequestParam final MultipartFile[] files,
                             @RequestParam final String categoryUrl, @RequestParam final String subgroupUrl,
                             final Model model) {
        final SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        projectService.save(project, subgroup);
        imageService.processImagesOnWrite(files, project);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupUrl);
    }

    @PostMapping("/admin/removeProject")
    public String removeProject(@RequestParam final String categoryUrl, @RequestParam final String subgroupUrl,
                                @RequestParam final String projectTitle) {
        projectService.deleteByTitle(projectTitle);
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupUrl);
    }

}
