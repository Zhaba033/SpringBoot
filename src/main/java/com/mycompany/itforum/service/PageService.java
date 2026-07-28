package com.mycompany.itforum.service;

import com.mycompany.itforum.dto.PageDTO;
import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.PostRepository;
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

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    Map<String, PageDTO> pages = new HashMap();

    // PRIVATE
    @PostConstruct
    private void initPages() {
        pages.put("home", new PageDTO("Главная", "/", null));
        pages.put("categories", new PageDTO("Категории", "/category-list", "home"));
        pages.put("profile", new PageDTO("Профиль", "/account/profile", "home"));
        pages.put("change-password", new PageDTO("Сменить пароль", "/account/change-password", "profile"));
    }
    
    private void addCategory(String catUid) {
        Category cat = categoryRepository.findByUid(catUid);
        if (!pages.containsKey(catUid)) {
            pages.put(catUid, new PageDTO(cat.getName(), "/categories/" + cat.getName(), "categories"));
        }
    }
    
    private void addPost(String postTitle) {
        Post post = postRepository.findByTitle(postTitle);
        if (!pages.containsKey(post.getTitle())) {
            Category postCat = postRepository.findCategoryById(post.getId());
            addCategory(postCat.getUid());
            pages.put(postTitle, new PageDTO(postTitle, "/categories/" + postCat.getName() + "/" + post.getId(), postCat.getUid()));
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
