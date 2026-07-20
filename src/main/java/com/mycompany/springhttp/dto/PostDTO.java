package com.mycompany.springhttp.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class PostDTO {
    
    String author;
    String category;
    String title;
    String body;
    String code;
    String id;
    
    String timeFromNow = "";
    LocalDateTime createdDate;
    
    Map<String, CommentDTO> comments = new HashMap<>();
}
