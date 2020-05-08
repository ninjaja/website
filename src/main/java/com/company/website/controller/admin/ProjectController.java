package com.company.website.controller.admin;

import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.service.CategoryService;
import com.company.website.service.ImageService;
import com.company.website.service.ProjectService;
import com.company.website.service.SubgroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
@AllArgsConstructor
public class ProjectController {

    private static final String REDIRECT_PROJECT = "redirect:/admin/%s/%s/%s";
    private static final String ADMIN_PROJECT = "admin/adminProject";

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;
    private final ImageService imageService;

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                              @PathVariable final String projectUrl, final ImageDTO imageDTO, final Model model) {
        final ProjectDTO projectDTO = projectService.findByUrl(projectUrl);
        return serveProjectPage(categoryUrl, subgroupUrl, projectDTO, imageDTO, model);
    }

    @PostMapping("/admin/editProject")
    public String editProject(@Valid final ProjectDTO projectDTO,
                              final BindingResult result, final ImageDTO imageDTO,
                              @RequestParam final String categoryUrl,
                              @RequestParam final String subgroupUrl, final Model model) {
        if(result.hasErrors()) {
            projectService.copyOnError(projectDTO);
            return serveProjectPage(categoryUrl, subgroupUrl, projectDTO, imageDTO, model);
        }
        final SubgroupDTO subgroupDTO = subgroupService.findByUrl(subgroupUrl);
        projectService.save(projectDTO, subgroupDTO);
        model.addAttribute("project", projectDTO);
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectDTO.getUrl());
    }

    @PostMapping("/admin/addImages")
    public String addImages(@RequestParam("files") final MultipartFile[] files, @RequestParam final String categoryUrl,
                            @RequestParam final String subgroupUrl, @RequestParam final String projectUrl,
                            final Model model) {
        final ProjectDTO projectDTO = projectService.findByUrl(projectUrl);
        imageService.processImagesOnWrite(files, projectDTO);
        model.addAttribute("images", imageService.findAllByProject(projectDTO));
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectUrl);
    }

    @PostMapping("/admin/removeImage")
    public String removeImage(@RequestParam final String categoryUrl,
                              @RequestParam final String subgroupUrl, @RequestParam final String projectUrl,
                              @RequestParam final String imageTitle) {
        imageService.removeImage(imageTitle);
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectUrl);
    }

    private String serveProjectPage(String categoryUrl, String subgroupUrl, ProjectDTO projectDTO,
                                    ImageDTO imageDTO, Model model) {
        model.addAttribute("categoryDTO", categoryService.findByUrl(categoryUrl));
        model.addAttribute("subgroupDTO", subgroupService.findByUrl(subgroupUrl));
        model.addAttribute("projectDTO", projectDTO);
        model.addAttribute("imageDTO", imageDTO);
        model.addAttribute("images", imageService.serveImagesOnRead(projectDTO));
        return ADMIN_PROJECT;
    }

}
