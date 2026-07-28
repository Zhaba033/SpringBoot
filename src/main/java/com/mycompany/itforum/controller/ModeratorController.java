package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.entity.Comment;
import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.CommentRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.AccountService;
import com.mycompany.itforum.service.PageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
public class ModeratorController {
    
    final PostRepository postRepository;
    final CategoryRepository categoryRepository;
    final CommentRepository commentRepository;
    
    
    @GetMapping("/moderator/panel")
    public String moderatorPanel() {
        return "moderator/moderator_panel";
    }
    @PostMapping("/moderator/categories/{c}/delete")
    public String removeCategory(@PathVariable String c) {
        categoryRepository.deleteByUid(c);
        return "redirect:/categories";
    }

    @PostMapping(value={"/moderator/post/{id}/delete", "/moderator/post/{id}/delete/{from}"})
    public String removePost(@PathVariable Long id, @PathVariable Optional<String> from) {
        Post post = postRepository.findById(id).orElseThrow();
        Category postCategory = post.getCategory();
        postRepository.delete(post);
        log.info(from.get());
        if (from.isPresent() && from.get().equals("home")) {
            return "redirect:/";
        }
        return "redirect:/categories/" + postCategory.getUid();
    }

    @PostMapping("/moderator/comment/{commId}/delete")
    public String removeComm(@PathVariable Long commId) {
        Comment comment = commentRepository.findById(commId).orElseThrow();
        commentRepository.delete(comment);
        return "redirect:/post/" + postRepository.findByCommentsId(commId).getId();
    }
}
