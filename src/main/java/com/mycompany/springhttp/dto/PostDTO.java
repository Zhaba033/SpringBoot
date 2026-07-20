package com.mycompany.springhttp.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class PostDTO {
    String category;
    String title;
    String short_desc;
    String full_desc;
    String code;
    
    String timeFromNow = "";
    LocalDateTime createdDate;
    
    Map<String, CommentDTO> comments = new HashMap<>();
    
    public void removeComment(String id) {
        comments.remove(id);
    }
}
