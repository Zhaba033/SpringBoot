package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.PageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {

    final PostRepository postRepository;
    final CategoryRepository categoryRepository;

    final PageService pageService;

    @GetMapping("/category/{c}")
    public String CategoryPage(
            @PathVariable String c,
            @ModelAttribute(name = "filter") String filter,
            Model model) {
        if (!categoryRepository.existsByUid(c)) {
            return "404";
        }

        model.addAttribute("breadcrumbs", pageService.getPages(c, "cat"));

        Category cat = categoryRepository.findByUid(c);
        model.addAttribute("category", cat);

        return "category/category_page";
    }

    @GetMapping("/category-list")
    public String allCatsPage(
            Model model
    ) {
        model.addAttribute("breadcrumbs", pageService.getPages("categories", ""));

        List<Category> cats = categoryRepository.findTop();
        model.addAttribute("categories", cats);
        return "category/category_list";
    }

    @GetMapping("/moderator/new-category")
    public String createCategoryPage(Model model) {
        return "category/create_category";
    }

    @PostMapping("/moderator/new-category/create")
    public String createCategory(Category cat, Model model) {

        // catch "Exceptions" (like id already exists)
        if (categoryRepository.existsByUid(cat.getUid())) {
            return "redirect:/moderator/new-category?alreadyExists=true";
        }

        categoryRepository.save(cat);
        return "redirect:/category-list";
    }
}
