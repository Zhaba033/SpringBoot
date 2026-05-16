package com.mycompany.springhttp.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    
    List<CommentDTO> comments = new ArrayList<>();
}
