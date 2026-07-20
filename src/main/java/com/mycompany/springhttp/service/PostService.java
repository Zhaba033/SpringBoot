package com.mycompany.springhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.springhttp.dto.CommentDTO;
import com.mycompany.springhttp.dto.PostDTO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostService {
    
    private int id = 1;
    
    private final ObjectMapper om;
    private Map<String, PostDTO> posts;
    private final File postsJson = new File("files/json/posts.json");
    
    // PRIVATE METHODS ------------------
    private void writeJson() {
        try {
            om.writerWithDefaultPrettyPrinter()
                    .writeValue(postsJson, posts);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void initJson() {
        try {
            posts = om.readValue(postsJson, new TypeReference<Map<String, PostDTO>>() {});
            for (String i : posts.keySet()) {
                id = Integer.valueOf(i);
            }
            id++;
        } catch (IOException e) {
            posts = new HashMap();
            log.error("Error: ", e);
        }
    }
    
    // CONSTRUCTOR ------------------
    public PostService(ObjectMapper objectMapper) {
        om = objectMapper;
        initJson();
    }
    
    // PUBLIC METHODS ------------------
    public Map<String, PostDTO> getMap() {
        return posts;
    }
    
    public Map<String, PostDTO> getPostsById(List<String> ids) {
        Map<String, PostDTO> result = new LinkedHashMap();
        
        for (String i : ids) {
            
            PostDTO curPost = posts.get(i);
            
            
            PrettyTime prettyTime = new PrettyTime(new Date());
            String timeFromNow = prettyTime.format(Date.from(curPost.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()));
            
            
            posts.get(i).setTimeFromNow(timeFromNow);
            result.put(i, curPost);
        }
        
        return result;
    }
    
    public int add(PostDTO post) {
        post.setCreatedDate(LocalDateTime.now());
        posts.put(String.valueOf(id), post);
        id++;
        writeJson();
        return id-1;
    }
    
    public void add_comment(String i, String us, String c) { // id, username, comment
        PostDTO post = posts.get(i);
        Map<String, CommentDTO> coms = post.getComments();
        CommentDTO newCom = new CommentDTO(us, c, LocalDateTime.now());
        newCom.setId(String.valueOf(coms.size()+1));
        coms.put(String.valueOf(coms.size()+1), newCom);
        post.setComments(coms);
        writeJson();
    }
    
    public List<CommentDTO> get_coms(String i) {
        PostDTO post = posts.get(i);
        List<CommentDTO> coms = new ArrayList<>();
        PrettyTime prettyTime = new PrettyTime(new Date());
        for (CommentDTO c : post.getComments().values()) {
            c.setTimeFromNow(prettyTime.format(Date.from(c.getCreatedTime().atZone(ZoneId.systemDefault()).toInstant())));
            coms.add(c);
        }
        Collections.reverse(coms);
        return coms;
    }
    
    // REMOVE
    
    public void removePost(String id) {
        posts.remove(id);
        writeJson();
    }
    
    public void removeComm(String postId, String commId) {
        posts.get(postId).removeComment(commId);
        writeJson();
    }
    
}
