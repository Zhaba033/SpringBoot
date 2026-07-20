package com.mycompany.springhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.springhttp.dto.CategoryDTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryService {
    
    private final ObjectMapper om;
    private Map<String, CategoryDTO> cats;
    private File catsJson = new File("files/json/categories.json");
    
    public CategoryService(ObjectMapper objectMapper) {
        om = objectMapper;
        initJson();
    }
    
    public Map<String, CategoryDTO> getMap() {
        return cats;
    }
    
    public void add(String shortname, CategoryDTO cat) {
        cats.put(shortname, cat);
        writeJson();
    }

    public Map<String, CategoryDTO> getCats() {
        return cats;
    }
    
    private void writeJson() {
        try {
            om.writerWithDefaultPrettyPrinter()
                    .writeValue(catsJson, cats);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void initJson() {
        try {
            cats = om.readValue(catsJson, new TypeReference<Map<String, CategoryDTO>>() {});
        } catch (IOException e) {
            cats = new HashMap();
            //log.error(e.getMessage());
        }
    }
    
    public Map<String, String> catsIdName() {
        Map<String, String> mapa = new HashMap();
        
        for (String i : cats.keySet()) {
            mapa.put(i, cats.get(i).getName());
        }
        
        return mapa;
    }
    
    public boolean alreadyExists(String id) {
        return cats.keySet().contains(id);
    }
    
    public void addPostToCat(String catId, String postId) {
        if (cats.get(catId).getPosts() == null) {
            cats.get(catId).setPosts(new ArrayList<>());
        }
        cats.get(catId).add_post(postId);
        writeJson();
    }
    
    // REMOVE
    
    public void removeCat(String catId) {
        cats.remove(catId);
        writeJson();
    }
    
    public void removePostFromCat(String cat, String post) {
        cats.get(cat).remove_post(post);
    }
    
    
    
}
