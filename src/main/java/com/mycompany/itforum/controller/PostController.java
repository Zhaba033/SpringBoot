package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Comment;
import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.CommentRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.AccountService;
import com.mycompany.itforum.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
public class PostController {

    final PostRepository postRepository;
    final CategoryRepository categoryRepository;
    final AccountRepository accountRepository;
    final CommentRepository commentRepository;

    final AccountService accountService;
    final PageService pageService;

    @GetMapping("/create-post")
    public String uploadPostPage(
            Model model,
            @ModelAttribute(name = "category") String category
    ) {
        model.addAttribute("category", category);
        model.addAttribute("cats", categoryRepository.findAll());
        return "post/create_post";
    }

    @PostMapping("/create-post/upload")
    public String uploadPost(@ModelAttribute Post post) {
        postRepository.save(post);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/post/{c}")
    public String PostPage(@PathVariable Long c,
            Model model) {

        if (!postRepository.existsById(c)) {
            return "404";
        }

        Post post = postRepository.findById(c).orElseThrow();
        
        model.addAttribute("breadcrumbs", pageService.getPages(post.getTitle(), "post"));
        
        model.addAttribute("post", post);
        model.addAttribute("id", c);
        model.addAttribute("comments", post.getComments());

        return "post/post_page";

    }

    @PostMapping("/post/{c}")
    public String PostPageComment(@PathVariable Long c, @ModelAttribute Comment comment) {
        
        comment.setUserRoles(accountRepository.findByCommentsId(comment.getId()).getRoles());
        
        commentRepository.save(comment);

        return "redirect:/post/" + c;

    }
}
