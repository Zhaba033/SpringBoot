
package com.mycompany.springhttp.dto;

import lombok.Data;

@Data
public class PageDTO {
    String name;
    String path;
    String prev;

    public PageDTO(String name, String path, String prev) {
        this.name = name;
        this.path = path;
        this.prev = prev;
    }
}
