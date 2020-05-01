package com.company.website.controller.admin;

import com.company.website.model.Category;
import com.company.website.model.Image;
import com.company.website.model.Project;
import com.company.website.model.Subgroup;
import com.company.website.repository.CategoryRepository;
import com.company.website.repository.ImageRepository;
import com.company.website.repository.ProjectRepository;
import com.company.website.repository.SubgroupRepository;
import com.company.website.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @PostMapping("/admin/addProject")
    public String addProject(@Valid Project project, @RequestParam MultipartFile[] files, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        project.setSubgroup(subgroup);
        project = projectRepository.save(project);
        for (MultipartFile file : files) {
            imageService.processImageOnWrite(file, project);
        }
        model.addAttribute("projects", projectRepository.findAllBySubgroup(subgroup));
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl();
    }

    @PostMapping("/admin/removeProject")
    public String removeProject(@RequestParam Integer categoryId, @RequestParam Integer subgroupId, @RequestParam Integer projectId) {
        projectRepository.deleteById(projectId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl();
    }

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable String categoryUrl, @PathVariable String subgroupUrl, @PathVariable String projectUrl, Model model) {
        Category category = categoryRepository.findByUrl(categoryUrl);
        Subgroup subgroup = subgroupRepository.findByUrl(subgroupUrl);
        Project project = projectRepository.findByUrl(projectUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("project", project);
        List<Image> images = imageRepository.findAllByProject(project);
        for(Image image : images) {
            image.setData(ImageService.applyDataOnRead(image));
        }
        model.addAttribute("images", images);
        return "admin/adminProject";
    }

    @PostMapping("/admin/editProject")
    public String editProject(@Valid Project project, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, @RequestParam Integer projectId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        project.setId(projectId);
        project.setSubgroup(subgroup);
        Project oldProject = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        if (!project.equals(oldProject)) {
            projectRepository.save(project);
            model.addAttribute("project", project);
            return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
        }
        model.addAttribute("project", oldProject);
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + oldProject.getUrl();
    }

    @PostMapping("/admin/addImages")
    public String addImages(@RequestParam("files") MultipartFile[] files, @RequestParam Integer categoryId, @RequestParam Integer subgroupId, @RequestParam Integer projectId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        for (MultipartFile file : files) {
            imageService.processImageOnWrite(file, project);
        }
        model.addAttribute("images", imageRepository.findAllByProject(project));
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
    }

    @PostMapping("/admin/removeImage")
    public String removeImage(@RequestParam Integer categoryId, @RequestParam Integer subgroupId, @RequestParam Integer projectId, @RequestParam Integer imageId) {
        imageService.removeImage(imageId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(EntityNotFoundException::new);
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        return "redirect:/admin/" + category.getUrl() + "/" + subgroup.getUrl() + "/" + project.getUrl();
    }




}
