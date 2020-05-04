package com.company.website.controller.admin;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Dmitry Matrizaev
 * @since 23.04.2020
 */
@Controller
public class ProjectController {

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

    @PostMapping("/admin/addProject")
    public String addProject(@Valid Project project, @RequestParam MultipartFile[] files,
                             @RequestParam Integer categoryId, @RequestParam Integer subgroupId, Model model) {
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupService.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        project.setSubgroup(subgroup);
        project = projectService.save(project);
        for (MultipartFile file : files) {
            imageService.processImageOnWrite(file, project);
        }
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl();
    }

    @PostMapping("/admin/removeProject")
    public String removeProject(@RequestParam Integer categoryId, @RequestParam Integer subgroupId,
                                @RequestParam Integer projectId) {
        projectService.deleteById(projectId);
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupService.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl();
    }

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}/{projectUrl}")
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
        return "admin/adminProject";
    }

    @PostMapping("/admin/editProject")
    public String editProject(@Valid Project project, @RequestParam Integer categoryId,
                              @RequestParam Integer subgroupId, @RequestParam Integer projectId, Model model) {
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupService.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        project.setId(projectId);
        project.setSubgroup(subgroup);
        Project oldProject = projectService.findById(projectId).orElseThrow(EntityNotFoundException::new);
        if (!project.equals(oldProject)) {
            projectService.save(project);
            model.addAttribute("project", project);
            return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
        }
        model.addAttribute("project", oldProject);
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + oldProject.getUrl();
    }

    @PostMapping("/admin/addImages")
    public String addImages(@RequestParam("files") MultipartFile[] files, @RequestParam Integer categoryId,
                            @RequestParam Integer subgroupId, @RequestParam Integer projectId, Model model) {
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupService.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        Project project = projectService.findById(projectId).orElseThrow(EntityNotFoundException::new);
        for (MultipartFile file : files) {
            imageService.processImageOnWrite(file, project);
        }
        model.addAttribute("images", imageService.findAllByProject(project));
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
    }

    @PostMapping("/admin/removeImage")
    public String removeImage(@RequestParam Integer categoryId, @RequestParam Integer subgroupId,
                              @RequestParam Integer projectId, @RequestParam Integer imageId) {
        imageService.removeImage(imageId);
        Category category = categoryService.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupService.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        Project project = projectService.findById(projectId).orElseThrow(EntityNotFoundException::new);
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
    }

}
