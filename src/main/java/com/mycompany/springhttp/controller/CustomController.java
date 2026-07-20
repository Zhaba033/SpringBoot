package com.mycompany.springhttp.controller;

import com.mycompany.springhttp.dto.AccountDTO;
import com.mycompany.springhttp.dto.CategoryDTO;
import com.mycompany.springhttp.dto.PostDTO;
import com.mycompany.springhttp.service.AccountService;
import com.mycompany.springhttp.service.CategoryService;
import com.mycompany.springhttp.service.PostService;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class CustomController {

    @Autowired
    private CategoryService servC;

    @Autowired
    private PostService servP;

    @Autowired
    private AccountService servA;

    // CATEGORIES
    @GetMapping("/categories/{c}")
    public String CategoryPage(@PathVariable String c,
            Model model) {
        if (!servC.getCats().keySet().contains(c)) {
            return "404";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO user = servA.getUserByName(auth.getName());
        model.addAttribute("roles", user.getRoles());

        CategoryDTO cat = servC.getCats().get(c);

        // define posts map
        if (cat.getPosts() != null) {

            List<String> postIds = new ArrayList<>(cat.getPosts());
            Collections.reverse(postIds);
            Map<String, PostDTO> posts = servP.getPostsById(postIds);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO user = servA.getUserByName(auth.getName());
        model.addAttribute("roles", user.getRoles());
        if (!servP.getMap().keySet().contains(c)) {
            return "404";
        }
        PostDTO post = servP.getMap().get(c);

        //date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatted = post.getCreatedDate().format(formatter);

        model.addAttribute("post", post);
        model.addAttribute("id", c);
        model.addAttribute("comments", servP.get_coms(c));
        model.addAttribute("date", formatted);

        return "post";

    }

    @PostMapping("/post/{c}")
    public String PostPageComment(@PathVariable String c,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "comment") String comment) {

        servP.add_comment(c, nickname, comment);

        return "redirect:/post/" + c;

    }

    // MODERATION
    @PostMapping("/moderator/categories/{c}/delete")
    public String removeCategory(@PathVariable String c) {
        servC.removeCat(c);
        return "redirect:/categories";
    }

    @PostMapping("/moderator/post/{id}/delete")
    public String removePost(@PathVariable String id) {
        log.info(servP.getMap().toString());
        String postCategory = servP.getMap().get(id).getCategory();
        servP.removePost(id);
        servC.removePostFromCat(postCategory, id);
        return "redirect:/categories/" + postCategory;
    }

    @PostMapping("/moderator/post/{postId}/comment/{commId}/delete")
    public String removeComm(@PathVariable String postId, @PathVariable String commId) {
        servP.removeComm(postId, commId);
        return "redirect:/post/" + postId;
    }

}
