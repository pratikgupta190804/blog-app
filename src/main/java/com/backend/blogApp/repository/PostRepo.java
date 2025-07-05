package com.backend.blogApp.repository;

import com.backend.blogApp.entity.Category;
import com.backend.blogApp.entity.Post;
import com.backend.blogApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

    List<Post> findByPostTitleContaining(String title);
}
