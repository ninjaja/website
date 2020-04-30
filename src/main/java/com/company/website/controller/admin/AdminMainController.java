package com.company.website.controller.admin;

import com.company.website.repository.CategoryRepository;
import com.company.website.repository.SubgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

@Controller
public class AdminMainController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubgroupRepository subgroupRepository;

    @GetMapping("/admin")
    public String greeting(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/adminHome";
    }



    @GetMapping("/admin/home")
    public String home() {
        return "redirect:/admin";
    }

}
