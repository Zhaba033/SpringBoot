package com.mycompany.itforum.repository;

import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.inter.CategoryTop;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    SELECT
        p.category AS category,
        COUNT(p) AS count
    FROM Post p
    GROUP BY p.category
    ORDER BY COUNT(p) DESC
    """)
    List<CategoryTop> findTopCategories(Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.createdTime DESC")
    List<Post> findRecent(Pageable pageable);
    
    //@Query(value="SELECT * FROM posts WHERE category_id=:categoryId ORDER BY posts.created_time DESC", nativeQuery = true)
    @Query("SELECT p FROM Post p WHERE p.category.id=:categoryId ORDER BY p.createdTime DESC")
    List<Post> findRecentPostsFromCategory(Pageable pageable, Long categoryId);

    List<Post> findByCategoryOrderByCreatedTimeDesc(Category category);

    Post findByTitle(String title);

}
