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
import java.util.List;

/**
 * @author Dmitry Matrizaev
 * @since 22.04.2020
 */
@Controller
@AllArgsConstructor
public class CategoryController {

    private static final String REDIRECT_TO_CATEGORY = "redirect:/admin/%s";
    private static final String CATEGORY = "admin/adminCategory";

    private final CategoryService categoryService;
    private final SubgroupService subgroupService;

    @GetMapping("/admin/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable final String categoryUrl, final SubgroupDTO subgroupDTO,
                               final Model model) {
        final CategoryDTO categoryDTO = categoryService.findByUrl(categoryUrl);
        return serveCategoryPage(categoryDTO, subgroupDTO, model);
    }

    @PostMapping("/admin/editCategory")
    public String editCategory(@Valid final CategoryDTO categoryDTO, final BindingResult result,
                               final SubgroupDTO subgroupDTO, final Model model) {
        if (result.hasErrors()) {
            categoryService.copyOnError(categoryDTO);
            return serveCategoryPage(categoryDTO, subgroupDTO, model);
        }
        categoryService.save(categoryDTO);
        model.addAttribute("category", categoryDTO);
        return String.format(REDIRECT_TO_CATEGORY, categoryDTO.getUrl());
    }

    @PostMapping("/admin/addSubgroup")
    public String addSubgroup(@Valid final SubgroupDTO subgroupDTO,
                              BindingResult result, @RequestParam final String categoryUrl,
                              final Model model) {
        CategoryDTO categoryDTO;
        if (result.hasErrors()) {
            categoryDTO = categoryService.findByUrl(categoryUrl);
            return serveCategoryPage(categoryDTO, subgroupDTO, model);
        }
        categoryDTO = categoryService.findByUrl(categoryUrl);
        subgroupService.save(subgroupDTO, categoryDTO);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(categoryDTO));
        return String.format(REDIRECT_TO_CATEGORY, categoryUrl);
    }

    @PostMapping("/admin/removeSubgroup")
    public String removeSubgroup(@RequestParam final String categoryUrl, @RequestParam final String subgroupUrl) {
        subgroupService.deleteByUrl(subgroupUrl);
        return String.format(REDIRECT_TO_CATEGORY, categoryUrl);
    }

    private String serveCategoryPage(CategoryDTO categoryDTO, SubgroupDTO subgroupDTO,
                                     Model model) {
        List<SubgroupDTO> subgroups = subgroupService.findAllByCategory(categoryDTO);
        model.addAttribute("categoryDTO", categoryDTO);
        model.addAttribute("subgroupDTO", subgroupDTO);
        model.addAttribute("subgroups", subgroups);
        return CATEGORY;
    }

}
