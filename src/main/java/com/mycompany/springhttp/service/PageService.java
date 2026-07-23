package com.mycompany.springhttp.service;

import com.mycompany.springhttp.dto.CategoryDTO;
import com.mycompany.springhttp.dto.PageDTO;
import com.mycompany.springhttp.dto.PostDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageService {

    private final PostService postService;
    private final CategoryService categoryService;

    Map<String, PageDTO> pages = new HashMap();

    // PRIVATE
    @PostConstruct
    private void initPages() {
        pages.put("home", new PageDTO("Главная", "/", null));
        pages.put("categories", new PageDTO("Категории", "/categories", "home"));
        pages.put("profile", new PageDTO("Профиль", "/account/profile", "home"));
        pages.put("change-password", new PageDTO("Сменить пароль", "/account/change-password", "profile"));
    }
    
    private void addCategory(String catName) {
        CategoryDTO cat = categoryService.getCategory(catName);
        if (!pages.containsKey(cat.getId())) {
            pages.put(cat.getId(), new PageDTO(cat.getName(), "/categories/" + catName, "categories"));
        }
    }
    
    private void addPost(String postId) {
        PostDTO post = postService.getPost(postId);
        if (!pages.containsKey(post.getId())) {
            CategoryDTO postCat = categoryService.getCategory(post.getCategory());
            addCategory(postCat.getId());
            pages.put(post.getId(), new PageDTO(post.getTitle(), "/categories/" + postCat.getName() + "/" + post.getId(), postCat.getId()));
        }
    }

    // PUBLIC
    public List<PageDTO> getPages(String pageName, String type) {
        List<PageDTO> res = new ArrayList();
        
        if (!type.isEmpty()) {
            switch (type) {
                case "cat":
                    addCategory(pageName);
                    break;
                case "post":
                    addPost(pageName);
                    break;
            }
        }

        PageDTO page = pages.get(pageName);
        res.add(page);
        while (page.getPrev() != null) {
            page = pages.get(page.getPrev());
            res.add(page);
        }
        Collections.reverse(res);
        return res;
    }

}
