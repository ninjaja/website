package com.company.website.controller.admin;

import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.service.ImageService;
import com.company.website.service.ProjectService;
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

import static com.company.website.controller.constants.ControllerConstants.ADMIN_PROJECT;
import static com.company.website.controller.constants.ControllerConstants.IMAGES;
import static com.company.website.controller.constants.ControllerConstants.IMAGE_DTO;
import static com.company.website.controller.constants.ControllerConstants.PROJECT_DTO;
import static com.company.website.controller.constants.ControllerConstants.REDIRECT_PROJECT;

/**
 * @author Dmitry Matrizaev
 * @since 23.04.2020
 */
@Controller
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ImageService imageService;

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                              @PathVariable final String projectUrl, final ImageDTO imageDTO, final Model model) {
        final ProjectDTO projectDTO = projectService.viewProject(projectUrl);
        return serveProjectPage(projectDTO, imageDTO, model);
    }

    @PostMapping("/admin/editProject")
    public String editProject(@Valid final ProjectDTO projectDTO,
                              final BindingResult result, final ImageDTO imageDTO,
                              @RequestParam final String categoryUrl,
                              @RequestParam final String subgroupUrl, final Model model) {
        if(result.hasErrors()) {
            projectService.copyDTOValuesOnEditError(projectDTO);
            return serveProjectPage(projectDTO, imageDTO, model);
        }
        projectService.save(projectDTO, subgroupUrl);
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectDTO.getUrl());
    }

    @PostMapping("/admin/addImages")
    public String addImages(@RequestParam("files") final MultipartFile[] files, @RequestParam final String categoryUrl,
                            @RequestParam final String subgroupUrl, @RequestParam final String projectUrl,
                            final Model model) {
        final ProjectDTO projectDTO = projectService.findByUrl(projectUrl);
        imageService.processImagesOnWrite(files, projectDTO);
        model.addAttribute(IMAGES, imageService.findAllByProject(projectDTO));
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectUrl);
    }

    @PostMapping("/admin/removeImage")
    public String removeImage(@RequestParam final String categoryUrl,
                              @RequestParam final String subgroupUrl, @RequestParam final String projectUrl,
                              @RequestParam final String imageTitle) {
        imageService.removeImage(imageTitle);
        return String.format(REDIRECT_PROJECT, categoryUrl, subgroupUrl, projectUrl);
    }

    private String serveProjectPage(ProjectDTO projectDTO, ImageDTO imageDTO, Model model) {
        model.addAttribute(PROJECT_DTO, projectDTO);
        model.addAttribute(IMAGE_DTO, imageDTO);
        model.addAttribute(IMAGES, projectDTO.getImages());
        return ADMIN_PROJECT;
    }

}
