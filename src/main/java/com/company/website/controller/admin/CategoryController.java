package com.company.website.controller.admin;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.service.CategoryService;
import com.company.website.service.SubgroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import static com.company.website.controller.constants.ControllerConstants.CATEGORY;
import static com.company.website.controller.constants.ControllerConstants.CATEGORY_DTO;
import static com.company.website.controller.constants.ControllerConstants.REDIRECT_TO_CATEGORY;
import static com.company.website.controller.constants.ControllerConstants.SUBGROUPS;
import static com.company.website.controller.constants.ControllerConstants.SUBGROUP_DTO;

/**
 * Admin console Categories page controller
 *
 * @author Dmitry Matrizaev
 * @since 22.04.2020
 */
@Controller
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;

    @GetMapping("/admin/{categoryUrl}")
    public String viewCategory(@PathVariable final String categoryUrl, final SubgroupDTO subgroupDTO,
                               final Model model) {
        model.addAttribute(SUBGROUP_DTO, subgroupDTO);
        model.addAttribute(CATEGORY_DTO, categoryService.viewCategory(categoryUrl));
        return CATEGORY;
    }

    @PostMapping("/admin/editCategory")
    public String editCategory(@Valid final CategoryDTO categoryDTO, final BindingResult result,
                               final SubgroupDTO subgroupDTO, final Model model) {
        if (result.hasErrors()) {
            categoryService.copyDTOValuesOnEditError(categoryDTO);
            return serveCategoryPage(categoryDTO, subgroupDTO, model);
        }
        categoryService.save(categoryDTO);
        model.addAttribute(CATEGORY_DTO, categoryDTO);
        return String.format(REDIRECT_TO_CATEGORY, categoryDTO.getUrl());
    }

    @PostMapping("/admin/addSubgroup")
    public String addSubgroup(@Valid final SubgroupDTO subgroupDTO, BindingResult result,
                              @RequestParam final String categoryUrl, final Model model) {
        if (result.hasErrors()) {
            final CategoryDTO categoryDTO = categoryService.findByUrl(categoryUrl);
            return serveCategoryPage(categoryDTO, subgroupDTO, model);
        }
        subgroupService.save(subgroupDTO, categoryUrl);
        model.addAttribute(SUBGROUPS, subgroupService.findAllByCategoryUrl(categoryUrl));
        return String.format(REDIRECT_TO_CATEGORY, categoryUrl);
    }

    @PostMapping("/admin/removeSubgroup")
    public String removeSubgroup(@RequestParam final String categoryUrl, @RequestParam final String subgroupUrl) {
        subgroupService.deleteByUrl(subgroupUrl);
        return String.format(REDIRECT_TO_CATEGORY, categoryUrl);
    }

    private String serveCategoryPage(final CategoryDTO categoryDTO, final SubgroupDTO subgroupDTO, final
                                     Model model) {
        model.addAttribute(CATEGORY_DTO, categoryDTO);
        model.addAttribute(SUBGROUP_DTO, subgroupDTO);
        model.addAttribute(SUBGROUPS, subgroupService.findAllByCategoryUrl(categoryDTO.getUrl()));
        return CATEGORY;
    }

}
