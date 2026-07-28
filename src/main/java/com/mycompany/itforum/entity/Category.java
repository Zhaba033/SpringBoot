package com.mycompany.itforum.entity;

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
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "categories")
public class Category {
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    Long id;
    
    @Column(nullable=false)
    String uid;
    
    @Column(nullable=false)
    String name;
    
    @Column(nullable=false)
    String description;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private List<Post> post;
    
}
