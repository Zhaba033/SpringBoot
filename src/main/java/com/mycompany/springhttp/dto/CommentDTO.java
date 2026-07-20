package com.mycompany.springhttp.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CommentDTO {
    String author;
    String comment;
    String id;
    
    List<String> userRoles;
    
    String timeFromNow = "";
    LocalDateTime createdTime;
}
