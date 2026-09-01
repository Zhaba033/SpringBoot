package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Account;
import com.mycompany.itforum.entity.Comment;
import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.CommentRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class PostController {

    final PostRepository postRepository;
    final CategoryRepository categoryRepository;
    final AccountRepository accountRepository;
    final CommentRepository commentRepository;
    
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
    public String uploadPost(@ModelAttribute Post post,
            @ModelAttribute(name = "category_uid") String uid) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = accountRepository.findByUsername(auth.getName());
        
        post.setAuthor(user);
        post.setCategory(categoryRepository.findByUid(uid));
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

        return "post/post_page";

    }

    @PostMapping("/post/{c}")
    public String PostPageComment(@PathVariable Long c, @ModelAttribute(name = "comment") String comment_body) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = accountRepository.findByUsername(auth.getName());
        
        Comment comment = new Comment();
        
        comment.setComment(comment_body);
        comment.setAuthor(user);
        comment.setPost(postRepository.findById(c).orElseThrow());
        
        commentRepository.save(comment);

        return "redirect:/post/" + c;

    }
}
