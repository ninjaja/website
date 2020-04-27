package com.company.website.controller;

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
public class MainController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubgroupRepository subgroupRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "home";
    }



    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

}
