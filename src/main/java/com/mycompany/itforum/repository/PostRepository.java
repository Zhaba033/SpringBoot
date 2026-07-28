package com.mycompany.itforum.repository;

import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.entity.Post;


import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    
    @Query("SELECT p FROM Post p ORDER BY p.сreatedTime DESC")
    List<Post> findRecent(Pageable pageable);
    List<Post> findByCategoryOrderByCreatedTimeDesc(String category);
    
    Category findCategoryById(Long id);
    
    Post findByTitle(String title);
    Post findByCommentsId(Long id);
    
}