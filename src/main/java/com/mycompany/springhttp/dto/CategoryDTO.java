package com.mycompany.springhttp.dto;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryDTO {
    String name;
    String description;
    List<String> posts;
    
    public void add_post(String s) {
        posts.add(s);
    }
}
