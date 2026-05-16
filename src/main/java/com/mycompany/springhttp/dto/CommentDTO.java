package com.mycompany.springhttp.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDTO {
    String username;
    String comment;
    
    String timeFromNow = "";
    LocalDateTime createdTime;

    public CommentDTO(String username, String comment, LocalDateTime createdTime) {
        this.username = username;
        this.comment = comment;
        this.createdTime = createdTime;
    }
    
    
    
}
