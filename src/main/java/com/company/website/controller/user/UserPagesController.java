package com.company.website.controller.user;

import com.company.website.dto.SubgroupDTO;
import com.company.website.service.CategoryService;
import com.company.website.service.ProjectService;
import com.company.website.service.SubgroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.company.website.controller.constants.ControllerConstants.CATEGORY_DTO;
import static com.company.website.controller.constants.ControllerConstants.IMAGE_PAGE;
import static com.company.website.controller.constants.ControllerConstants.PROJECTS;
import static com.company.website.controller.constants.ControllerConstants.SUBGROUP_DTO;
import static com.company.website.controller.constants.ControllerConstants.USER_CATEGORY;
import static com.company.website.controller.constants.ControllerConstants.USER_HOMEPAGE;
import static com.company.website.controller.constants.ControllerConstants.USER_PROJECT;
import static com.company.website.controller.constants.ControllerConstants.USER_SUBGROUP;

/**
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
@AllArgsConstructor
public class UserPagesController {

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;

    @GetMapping("/")
    public String greeting(final Model model) {
        model.addAttribute(PROJECTS, projectService.findAllWithImages());
        return USER_HOMEPAGE;
    }

    @GetMapping("/{categoryUrl}")
    public String viewCategory(@PathVariable final String categoryUrl, final Model model) {
        model.addAttribute(CATEGORY_DTO, categoryService.viewCategory(categoryUrl));
        model.addAttribute(PROJECTS, projectService.findAllWithImagesByCategoryUrl(categoryUrl));
        return USER_CATEGORY;
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                               final Model model) {
        final SubgroupDTO subgroupDTO = subgroupService.viewSubgroupUserPages(subgroupUrl);
        model.addAttribute(SUBGROUP_DTO, subgroupDTO);
        model.addAttribute(CATEGORY_DTO, subgroupDTO.getCategory());
        model.addAttribute(PROJECTS, subgroupDTO.getProjects());
        return USER_SUBGROUP;
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                              @PathVariable final String projectUrl, @RequestParam final Optional<Integer> page,
                              final Model model) {
        model.addAttribute(IMAGE_PAGE, projectService.serveProjectToUser(projectUrl, page));
        return USER_PROJECT;
    }

}
