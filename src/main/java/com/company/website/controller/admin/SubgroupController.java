package com.company.website.controller.admin;

import com.company.website.dto.CategoryDTO;
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
public class SubgroupController {

    private static final String REDIRECT_SUBGROUP = "redirect:/admin/%s/%s";
    private static final String ADMIN_SUBGROUP = "admin/adminSubgroup";

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;
    private final ProjectService projectService;
    private final ImageService imageService;

    @GetMapping("/admin/{categoryUrl}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                               final ProjectDTO projectDTO,
                               final Model model) {
        final CategoryDTO categoryDTO = categoryService.findByUrl(categoryUrl);
        final SubgroupDTO subgroupDTO = subgroupService.findByUrl(subgroupUrl);
        return serveSubgroupPage(categoryDTO, subgroupDTO, projectDTO,model);
    }

    @PostMapping("/admin/editSubgroup")
    public String editSubgroup(@Valid final SubgroupDTO subgroupDTO,
                               final BindingResult result, final ProjectDTO projectDTO,
                               @RequestParam final String categoryUrl,
                               final Model model) {
        CategoryDTO categoryDTO;
        if (result.hasErrors()) {
            categoryDTO = categoryService.findByUrl(categoryUrl);
            return serveSubgroupPage(categoryDTO, subgroupDTO, projectDTO, model);
        }
        categoryDTO = categoryService.findByUrl(categoryUrl);
        subgroupService.save(subgroupDTO, categoryDTO);
        model.addAttribute("subgroupDTO", subgroupDTO);
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupDTO.getUrl());
    }

    @PostMapping("/admin/addProject")
    public String addProject(@Valid final ProjectDTO projectDTO,
                             BindingResult result, @RequestParam final MultipartFile[] files,
                             @RequestParam final String categoryUrl, @RequestParam final String subgroupUrl,
                             final Model model) {
        SubgroupDTO subgroupDTO;
        if (result.hasErrors()) {
            final CategoryDTO categoryDTO = categoryService.findByUrl(categoryUrl);
            subgroupDTO = subgroupService.findByUrl(subgroupUrl);
            return serveSubgroupPage(categoryDTO, subgroupDTO, projectDTO, model);
        }
        subgroupDTO = subgroupService.findByUrl(subgroupUrl);
        projectService.save(projectDTO, subgroupDTO);
        imageService.processImagesOnWrite(files, projectDTO);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroupDTO));
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupUrl);
    }

    @PostMapping("/admin/removeProject")
    public String removeProject(@RequestParam final String categoryUrl, @RequestParam final String subgroupUrl,
                                @RequestParam final String projectTitle) {
        projectService.deleteByTitle(projectTitle);
        return String.format(REDIRECT_SUBGROUP, categoryUrl, subgroupUrl);
    }

    private String serveSubgroupPage(CategoryDTO categoryDTO, SubgroupDTO subgroupDTO, ProjectDTO projectDTO,
                                     Model model) {
        model.addAttribute("categoryDTO", categoryDTO);
        model.addAttribute("subgroupDTO", subgroupDTO);
        model.addAttribute("projectDTO", projectDTO);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroupDTO));
        return ADMIN_SUBGROUP;
    }

}
