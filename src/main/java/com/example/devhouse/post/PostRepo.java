package com.example.devhouse.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
    Post findPostByPostId(Long postId);
}
