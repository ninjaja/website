package com.company.website.controller.user;

import com.company.website.model.Category;
import com.company.website.model.Image;
import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.service.CategoryService;
import com.company.website.service.ImageService;
import com.company.website.service.ProjectService;
import com.company.website.service.SubgroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
    public String greeting(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "user/userHome";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable String categoryUrl, Model model) {
        Category category = categoryService.findByUrl(categoryUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(category));
        return "user/userCategory";
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable String categoryUrl, @PathVariable String subgroupUrl, Model model) {
        Category category = categoryService.findByUrl(categoryUrl);
        Subgroup subgroup = subgroupService.findByUrl(subgroupUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return "user/userSubgroup";
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable String categoryUrl, @PathVariable String subgroupUrl,
                              @PathVariable String projectUrl, Model model) {
        Category category = categoryService.findByUrl(categoryUrl);
        Subgroup subgroup = subgroupService.findByUrl(subgroupUrl);
        Project project = projectService.findByUrl(projectUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("project", project);
        List<Image> images = imageService.findAllByProject(project);
        for (Image image : images) {
            image.setData(ImageService.applyDataOnRead(image));
        }
        model.addAttribute("images", images);
        return "user/userProject";
    }

}
