package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.CommentRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.AccountService;
import com.mycompany.itforum.service.PageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {

    final PostRepository postRepository;
    final CategoryRepository categoryRepository;
    final CommentRepository commentRepository;
    final AccountRepository accountRepository;

    final AccountService accountService;
    final PageService pageService;

    @GetMapping("/api/category/{cat}/posts/{page}")
    public List<Post> getPostsOnPage(
            @PathVariable Long cat,
            @PathVariable int page
    ) {
        
        return postRepository.findRecentPostsFromCategory(PageRequest.of(page+1, 5), cat);
        
    }

}
