package com.mycompany.itforum.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Table(name = "categories")

@Getter
@Setter
@NoArgsConstructor
public class Category {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    Long id;

    @Column(nullable = false)
    String uid;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Post> posts;

}
