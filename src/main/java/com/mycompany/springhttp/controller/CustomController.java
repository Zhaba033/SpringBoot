package com.mycompany.springhttp.controller;

import com.mycompany.springhttp.dto.CategoryDTO;
import com.mycompany.springhttp.dto.PostDTO;
import com.mycompany.springhttp.service.AccountService;
import com.mycompany.springhttp.service.CategoryService;
import com.mycompany.springhttp.service.PageService;
import com.mycompany.springhttp.service.PostService;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final PageService pageService;

    // CATEGORIES
    @GetMapping("/categories/{c}")
    public String CategoryPage(@PathVariable String c,
            Model model) {
        if (!categoryService.getCategories().keySet().contains(c)) {
            return "404";
        }
        
        model.addAttribute("breadcrumbs", pageService.getPages(c, "cat"));

        CategoryDTO cat = categoryService.getCategory(c);

        // define posts map
        if (cat.getPosts() != null) {

            List<String> postIds = new ArrayList<>(cat.getPosts());
            Collections.reverse(postIds);
            Map<String, PostDTO> posts = postService.getPostsById(postIds);
            model.addAttribute("posts", posts);
        }

        // time
        model.addAttribute("category", cat);

        return "category";
    }

    // POSTS
    @GetMapping("/post/{c}")
    public String PostPage(@PathVariable String c,
        Model model) {

        if (!postService.getPosts().keySet().contains(c)) {
            return "404";
        }

        model.addAttribute("breadcrumbs", pageService.getPages(c, "post"));

        PostDTO post = postService.getPost(c);

        //date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatted = post.getCreatedDate().format(formatter);

        model.addAttribute("post", post);
        model.addAttribute("id", c);
        model.addAttribute("comments", postService.get_coms(c));
        model.addAttribute("date", formatted);

        return "post";

    }

    @PostMapping("/post/{c}")
    public String PostPageComment(@PathVariable String c,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "comment") String comment) {

        postService.add_comment(c, username, comment, accountService.getUserByName(username).getRoles());

        return "redirect:/post/" + c;

    }

    // MODERATION
    @PostMapping("/moderator/categories/{c}/delete")
    public String removeCategory(@PathVariable String c) {
        categoryService.removeCat(c);
        return "redirect:/categories";
    }

    @PostMapping(value={"/moderator/post/{id}/delete", "/moderator/post/{id}/delete/{from}"})
    public String removePost(@PathVariable String id, @PathVariable Optional<String> from) {
        String postCategory = postService.getPost(id).getCategory();
        postService.removePost(id);
        categoryService.removePostFromCat(postCategory, id);
        log.info(from.get());
        if (from.isPresent() && from.get().equals("home")) {
            return "redirect:/";
        }
        return "redirect:/categories/" + postCategory;
    }

    @PostMapping("/moderator/post/{postId}/comment/{commId}/delete")
    public String removeComm(@PathVariable String postId, @PathVariable String commId) {
        postService.removeComm(postId, commId);
        return "redirect:/post/" + postId;
    }
}
