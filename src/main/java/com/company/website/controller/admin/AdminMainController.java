package com.company.website.controller.admin;

import com.company.website.dto.CategoryDTO;
import com.company.website.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import static com.company.website.controller.constants.ControllerConstants.ADMIN_HOME;
import static com.company.website.controller.constants.ControllerConstants.REDIRECT_TO_ADMIN;

/**
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Controller
@AllArgsConstructor
public class AdminMainController {

    private final CategoryService categoryService;

    @GetMapping("/admin")
    public String showAdminMain(final CategoryDTO categoryDTO, final Model model) {
        return serveAdminPage(categoryDTO, model);
    }

    @PostMapping("/admin/addCategory")
    public String addCategory(@Valid final CategoryDTO categoryDTO, final BindingResult result, final Model model) {
        if (result.hasErrors()) {
            return serveAdminPage(categoryDTO, model);
        }
        categoryService.save(categoryDTO);
        return REDIRECT_TO_ADMIN;
    }

    @PostMapping("/admin/removeCategory")
    public String removeCategory(@RequestParam final String title) {
        categoryService.deleteByTitle(title);
        return REDIRECT_TO_ADMIN;
    }

    private String serveAdminPage(CategoryDTO categoryDTO, Model model) {
        model.addAttribute("categoryDTO", categoryDTO);
        model.addAttribute("categories", categoryService.findAll());
        return ADMIN_HOME;
    }

}
