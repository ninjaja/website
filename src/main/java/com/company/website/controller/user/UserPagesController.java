package com.company.website.controller.user;

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

/**
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
public class UserPagesController {

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;
    private final ImageService imageService;

    public UserPagesController(CategoryService categoryService, SubgroupService subgroupService,
                               ProjectService projectService, ImageService imageService) {
        this.categoryService = categoryService;
        this.subgroupService = subgroupService;
        this.projectService = projectService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String greeting(final Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "user/userHome";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable final String categoryUrl, final Model model) {
        CategoryDTO category = categoryService.findByUrl(categoryUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(category));
        return "user/userCategory";
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                               final Model model) {
        CategoryDTO category = categoryService.findByUrl(categoryUrl);
        SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return "user/userSubgroup";
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                              @PathVariable final String projectUrl, final Model model) {
        CategoryDTO category = categoryService.findByUrl(categoryUrl);
        SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        ProjectDTO project = projectService.findByUrl(projectUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("project", project);
        model.addAttribute("images", imageService.serveImagesOnRead(project));
        return "user/userProject";
    }

}
