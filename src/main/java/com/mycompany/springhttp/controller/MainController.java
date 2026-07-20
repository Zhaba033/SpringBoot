package com.mycompany.springhttp.controller;

import com.mycompany.springhttp.dto.AccountDTO;
import com.mycompany.springhttp.dto.CategoryDTO;
import com.mycompany.springhttp.dto.PostDTO;
import com.mycompany.springhttp.service.AccountService;
import com.mycompany.springhttp.service.CategoryService;
import com.mycompany.springhttp.service.PostService;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AccountService servA;

    // HOME
    @GetMapping("/")
    public String HomePage() {
        return "home";
    }
    
    @PostMapping("/")
    public String HomePage(
            @RequestParam(name = "id") String id,
            Model model
    ) {
        Set<String> postsNum = servP.getMap().keySet();
        if (postsNum.contains(id)) {
            return "redirect:/post/" + id;
        } else {
            model.addAttribute("PostNE", id);
            return "home";
        }
    }

    
    // -------------------------
    // POSTS
    @GetMapping("/theme")
    public String uploadPostPage(Model model) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO user = servA.getUserByName(auth.getName());
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("cats", servC.catsIdName());
        return "upload_theme";
    }

    @PostMapping("/theme_upload")
    public String uploadPost(@ModelAttribute PostDTO post) {
        servC.addPostToCat(post.getCategory(), Integer.toString(servP.add(post)));
        return "redirect:/";
    }

    
    // -------------------------
    // CATS
    @GetMapping("/new-category")
    public String createCategoryPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO user = servA.getUserByName(auth.getName());
        model.addAttribute("roles", user.getRoles());
        return "create_category";
    }

    @PostMapping("/new-category/create")
    public String createCategory(
            CategoryDTO cat,
            @RequestParam(name = "category_id") String catId, 
            Model model)
    {
        
        // catch "Exceptions" (like id already exists)
        if (servC.alreadyExists(catId)) {
            model.addAttribute("alreadyExists", catId);
            return "create_category";
        }
        
        servC.add(catId, cat);
        return "redirect:/categories";
    }

    @GetMapping("/categories")
    public String allCatsPage(Model model) {
        //log.info(servC.getCats().toString());
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO user = servA.getUserByName(auth.getName());
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("categories", servC.getCats());
        return "categories";
    }
}
