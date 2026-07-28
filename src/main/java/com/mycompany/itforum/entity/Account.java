package com.mycompany.itforum.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

@Slf4j
@Entity
@Table(name = "accounts")

@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @ElementCollection
    List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    List<Comment> comments;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    List<Post> posts;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdTime;
}
