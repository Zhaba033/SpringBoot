package com.mycompany.springhttp.controller;

import com.mycompany.springhttp.dto.CategoryDTO;
import com.mycompany.springhttp.dto.PostDTO;
import com.mycompany.springhttp.service.CategoryService;
import com.mycompany.springhttp.service.PageService;
import com.mycompany.springhttp.service.PostService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private PostService servP;
    @Autowired
    private CategoryService servC;
    @Autowired
    private PageService servG;

    @GetMapping("/test")
    public String TestPage(Model model) {
        return "test";
    }
    
    // HOME
    @GetMapping("/")
    public String HomePage(Model model) {
        model.addAttribute("breadcrumbs", servG.getPages("home", ""));
        
        List<PostDTO> recentPosts = servP.getRecentPosts(3);
        model.addAttribute("posts", recentPosts);

        List<CategoryDTO> topCategories = servC.getTopCategories(3);
        model.addAttribute("categories", topCategories);

        return "home";
    }

    // -------------------------
    // POSTS
    @GetMapping("/create-post")
    public String uploadPostPage(
            Model model,
            @ModelAttribute(name = "category") String category
    ) {
        model.addAttribute("category", category);
        model.addAttribute("cats", servC.catsIdName());
        return "upload_theme";
    }

    @PostMapping("/create-post/upload")
    public String uploadPost(@ModelAttribute PostDTO post) {
        servC.addPostToCat(post.getCategory(), Integer.toString(servP.add(post)));
        return "redirect:/post/" + post.getId();
    }

    // -------------------------
    // CATS
    @GetMapping("/moderator/new-category")
    public String createCategoryPage(Model model) {
        return "create_category";
    }

    @PostMapping("/moderator/new-category/create")
    public String createCategory(
            CategoryDTO cat,
            @RequestParam(name = "category_id") String catId,
            Model model) {

        // catch "Exceptions" (like id already exists)
        if (servC.alreadyExists(catId)) {
            model.addAttribute("alreadyExists", catId);
            return "create_category";
        }

        servC.add(catId, cat);
        return "redirect:/categories";
    }
    
    @GetMapping("/moderator/panel")
    public String moderatorPanel() {
        return "moderator_panel";
    }

    @GetMapping("/categories")
    public String allCatsPage(
            Model model,
            @ModelAttribute(name = "filter") String filter
    ) {
        model.addAttribute("breadcrumbs", servG.getPages("categories", ""));
        
        //log.info(servC.getCats().toString());
        model.addAttribute("filter", filter);
        model.addAttribute("categories", servC.getCatsWithFilter(filter));
        return "categories";
    }

    @GetMapping("/result")
    public String result(
            @ModelAttribute(name = "code") String code,
            @ModelAttribute(name = "text") String text,
            Model model
    ) {
        if (code.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("code", code);
        model.addAttribute("text", text);
        return "result";
    }
}
