package com.mycompany.springhttp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class AccountDTO {
    String username;
    String password;
    
    List<String> roles;
    
    LocalDateTime createdTime;
    
    public void add_role(String role) {
        roles.add(role);
    }
}
