package com.company.website.controller.admin;

import com.company.website.dto.ProjectDTO;
import com.company.website.dto.SubgroupDTO;
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

import static com.company.website.controller.constants.ControllerConstants.ADMIN_SUBGROUP;
import static com.company.website.controller.constants.ControllerConstants.PROJECTS;
import static com.company.website.controller.constants.ControllerConstants.PROJECT_DTO;
import static com.company.website.controller.constants.ControllerConstants.REDIRECT_SUBGROUP;
import static com.company.website.controller.constants.ControllerConstants.SUBGROUP_DTO;

/**
 * @author Dmitry Matrizaev
 * @since 23.04.2020
 */
@Controller
@AllArgsConstructor
public class SubgroupController {

    private final SubgroupService subgroupService;
    private final ProjectService projectService;

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                               final ProjectDTO projectDTO,
                               final Model model) {
        return serveSubgroupPage(subgroupUrl, projectDTO, model);
    }

    @PostMapping("/admin/editSubgroup")
    public String editSubgroup(@Valid final SubgroupDTO subgroupDTO,
                               final BindingResult result, final ProjectDTO projectDTO,
                               @RequestParam final String categoryUrl,
                               final Model model) {
        if (result.hasErrors()) {
            subgroupService.copyDTOValuesOnEditError(subgroupDTO);
            return serveSubgroupPage(subgroupDTO.getUrl(), projectDTO, model);
        }
        subgroupService.save(subgroupDTO, categoryUrl);
        model.addAttribute(SUBGROUP_DTO, subgroupDTO);
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupDTO.getUrl());
    }

    @PostMapping("/admin/addProject")
    public String addProject(@Valid final ProjectDTO projectDTO,
                             BindingResult result, @RequestParam final MultipartFile[] files,
                             @RequestParam final String categoryUrl, @RequestParam final String subgroupUrl,
                             final Model model) {
        if (result.hasErrors()) {
            return serveSubgroupPage(subgroupUrl, projectDTO, model);
        }
        projectService.save(projectDTO, subgroupUrl, files);
        model.addAttribute(PROJECTS, projectService.findAllBySubgroupUrl(subgroupUrl));
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupUrl);
    }

    @PostMapping("/admin/removeProject")
    public String removeProject(@RequestParam final String categoryUrl, @RequestParam final String subgroupUrl,
                                @RequestParam final String projectTitle) {
        projectService.deleteByTitle(projectTitle);
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupUrl);
    }

    private String serveSubgroupPage(final String subgroupUrl, final ProjectDTO projectDTO, Model model) {
        final SubgroupDTO subgroupDTO = subgroupService.viewSubgroup(subgroupUrl);
        model.addAttribute(SUBGROUP_DTO, subgroupDTO);
        model.addAttribute(PROJECT_DTO, projectDTO);
        model.addAttribute(PROJECTS, subgroupDTO.getProjects());
        return ADMIN_SUBGROUP;
    }

}
