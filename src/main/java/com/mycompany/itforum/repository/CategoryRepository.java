package com.mycompany.itforum.repository;

import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
           SELECT c
           FROM Category c
           LEFT JOIN c.posts p
           GROUP BY c.id
           ORDER BY COUNT(p) DESC
           """)
    List<Category> findTop();
    
    Category findByUid(String uid);
    
    boolean existsByUid(String uid);
    
    void deleteByUid(String uid);
}