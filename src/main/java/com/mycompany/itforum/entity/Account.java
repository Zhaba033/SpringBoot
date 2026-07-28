package com.mycompany.itforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    
    @Column(nullable = false)
    String username;
    
    @Column(nullable = false)
    String password;
    
    @Column
    List<String> roles = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    List<Comment> comments;
    
    @Column
    LocalDateTime createdTime;
}
