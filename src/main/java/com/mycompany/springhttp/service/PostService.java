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
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostService {
    
    private int id = 1;
    
    private Map<String, PostDTO> posts;
    private final File postsJson = new File("files/json/posts.json");
    
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private final CategoryService categoryService;
    
    // PRIVATE METHODS ------------------
    private void writeJson() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(postsJson, posts);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    @PostConstruct
    private void initJson() {
        try {
            posts = objectMapper.readValue(postsJson, new TypeReference<Map<String, PostDTO>>() {});
            for (String i : posts.keySet()) {
                id = Integer.valueOf(i);
            }
            id++;
        } catch (IOException e) {
            posts = new HashMap();
            log.error("Error: ", e);
        }
    }
    
    
    // Обновляет всю информацию сразу! Не оптимизировано!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    
    private void updatePostsData() {
        PrettyTime prettyTime = new PrettyTime(new Date());
        for (Map.Entry<String, PostDTO> entry : posts.entrySet()) {
            entry.getValue().setTimeFromNow(prettyTime.format(Date.from(entry.getValue().getCreatedDate().atZone(ZoneId.systemDefault()).toInstant())));
            updateCommsData(entry.getValue().getId());
        }
    }
    
    private void updateCommsData(String postId) { // обновляет информацию в комментариях: роли пользователя, время с публикации
        PrettyTime prettyTime = new PrettyTime(new Date());
        PostDTO post = posts.get(postId);
        for (Map.Entry<String, CommentDTO> entry : post.getComments().entrySet()) {
            entry.getValue().setTimeFromNow(prettyTime.format(Date.from(entry.getValue().getCreatedTime().atZone(ZoneId.systemDefault()).toInstant())));
            entry.getValue().setUserRoles(accountService.getUserRoles(entry.getValue().getAuthor()));
        }
    }
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    /*
    // CONSTRUCTOR ------------------
    public PostService(ObjectMapper objectMapper) {
        om = objectMapper;
        initJson();
    }*/
    
    // PUBLIC METHODS ------------------
    public Map<String, PostDTO> getPosts() {
        return posts;
    }
    
    public PostDTO getPost(String id) {
        return posts.get(id);
    }
    
    public Map<String, PostDTO> getPostsById(List<String> ids) {
        updatePostsData();
        Map<String, PostDTO> result = new LinkedHashMap();
        
        for (String i : ids) {
            PostDTO curPost = posts.get(i);
            result.put(i, curPost);
        }
        
        return result;
    }
    
    public void add(PostDTO post) {
        post.setCreatedDate(LocalDateTime.now());
        post.setId(String.valueOf(id));
        posts.put(String.valueOf(id), post);
        categoryService.addPostToCat(post.getCategory(), Integer.toString(id));
        id++;
        writeJson();
    }
    
    public void add_comment(String i, String us, String c, List<String> userRoles) { // id, username, comment
        PostDTO post = posts.get(i);
        Map<String, CommentDTO> coms = post.getComments();
        CommentDTO newCom = new CommentDTO();
        
        newCom.setAuthor(us);
        newCom.setComment(c);
        newCom.setCreatedTime(LocalDateTime.now());
        newCom.setUserRoles(userRoles);
        
        newCom.setId(String.valueOf(coms.size()+1));
        coms.put(String.valueOf(coms.size()+1), newCom);
        post.setComments(coms);
        writeJson();
    }
    
    public List<CommentDTO> get_coms(String i) {
        updateCommsData(i);
        List<CommentDTO> coms = new ArrayList(posts.get(i).getComments().values());
        Collections.reverse(coms);
        return coms;
    }
    
    public List<PostDTO> getRecentPosts(int count) {
        updatePostsData();
        List<PostDTO> p = new ArrayList(posts.values());
        if (count > posts.size()) {
            count = posts.size();
        }
        Collections.reverse(p);
        p = p.subList(0, count);
        return p;
    }
    
    
    // REMOVE
    
    public void removePost(String id) {
        posts.remove(id);
        writeJson();
    }
    
    public void removeComm(String postId, String commId) {
        posts.get(postId).getComments().remove(commId);
        writeJson();
    }
    
}
