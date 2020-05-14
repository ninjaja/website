package com.company.website.controller.user;

import com.company.website.dto.CategoryDTO;
import com.company.website.dto.ImageDTO;
import com.company.website.dto.ProjectDTO;
import com.company.website.dto.SubgroupDTO;
import com.company.website.service.CategoryService;
import com.company.website.service.ImageService;
import com.company.website.service.ProjectService;
import com.company.website.service.SubgroupService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final ImageService imageService;

    @GetMapping("/")
    public String greeting(final ProjectDTO projectDTO, final Model model) {
        model.addAttribute("projects", projectService.findAllWithImages());
        model.addAttribute("projectDTO", projectDTO);
        return USER_HOMEPAGE;
    }

    @GetMapping("/{categoryUrl:^(?!favicon).+}")
    public String viewCategory(@PathVariable final String categoryUrl, final Model model) {
        final CategoryDTO categoryDTO = categoryService.findByUrl(categoryUrl);
        model.addAttribute("categoryDTO", categoryDTO);
        model.addAttribute("subgroups", subgroupService.findAllByCategory(categoryDTO));
        return USER_CATEGORY;
    }

    @GetMapping("/{categoryUrl:^(?!.*(?:css|img)).+}/{subgroupUrl}")
    public String viewSubgroup(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                               final Model model) {
        final CategoryDTO category = categoryService.findByUrl(categoryUrl);
        final SubgroupDTO subgroup = subgroupService.findByUrl(subgroupUrl);
        model.addAttribute("category", category);
        model.addAttribute("subgroup", subgroup);
        model.addAttribute("projects", projectService.findAllBySubgroup(subgroup));
        return USER_SUBGROUP;
    }

    @GetMapping("/{categoryUrl}/{subgroupUrl}/{projectUrl}")
    public String viewProject(@PathVariable final String categoryUrl, @PathVariable final String subgroupUrl,
                              @PathVariable final String projectUrl, @RequestParam final Optional<Integer> page,
                              final Model model) {
        final CategoryDTO categoryDTO = categoryService.findByUrl(categoryUrl);
        final SubgroupDTO subgroupDTO = subgroupService.findByUrl(subgroupUrl);
        final ProjectDTO projectDTO = projectService.findByUrl(projectUrl);
        final int currentPage = page.orElse(1);
        final PageRequest pageable = PageRequest.of(currentPage - 1, 1);
        final Page<ImageDTO> imageDTOPage = imageService.serveImagesOnReadPaginated(projectDTO, pageable);
        final int totalPages = imageDTOPage.getTotalPages();
        if (totalPages > 0) {
            final List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("categoryDTO", categoryDTO);
        model.addAttribute("subgroupDTO", subgroupDTO);
        model.addAttribute("projectDTO", projectDTO);
        model.addAttribute("imagePage", imageDTOPage);
        return USER_PROJECT;
    }

}
