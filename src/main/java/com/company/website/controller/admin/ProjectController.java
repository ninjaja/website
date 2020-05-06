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
public class ProjectController {

    private static final String REDIRECT_PROJECT = "redirect:/admin/%s/%s/%s";

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;
    private final ImageService imageService;

    public ProjectController(CategoryService categoryService, SubgroupService subgroupService,
                             ProjectService projectService, ImageService imageService) {
        this.categoryService = categoryService;
        this.subgroupService = subgroupService;
        this.projectService = projectService;
        this.imageService = imageService;
    }

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                              @PathVariable final String projectUrl, final Model model) {
        final CategoryDTO category = categoryService.findByUrl(categoryUrl);
        final SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        final ProjectDTO project = projectService.findByUrl(projectUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("project", project);
        model.addAttribute("images", imageService.serveImagesOnRead(project));
        return "admin/adminProject";
    }

    @PostMapping("/admin/editProject")
    public String editProject(@Valid final ProjectDTO project, @RequestParam final String categoryUrl,
                              @RequestParam final String subgroupUrl, final Model model) {
        final SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        projectService.save(project, subgroup);
        model.addAttribute("project", project);
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, project.getUrl());
    }

    @PostMapping("/admin/addImages")
    public String addImages(@RequestParam("files") final MultipartFile[] files, @RequestParam final String categoryUrl,
                            @RequestParam final String subgroupUrl, @RequestParam final String projectUrl,
                            final Model model) {
        final ProjectDTO project = projectService.findByUrl(projectUrl);
        imageService.processImagesOnWrite(files, project);
        model.addAttribute("images", imageService.findAllByProject(project));
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectUrl);
    }

    @PostMapping("/admin/removeImage")
    public String removeImage(@RequestParam final String categoryUrl,
                              @RequestParam final String subgroupUrl, @RequestParam final String projectUrl,
                              @RequestParam final String imageTitle) {
        imageService.removeImage(imageTitle);
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectUrl);
    }

}
