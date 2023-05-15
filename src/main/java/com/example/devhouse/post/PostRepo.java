package com.example.devhouse.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    Post findPostByPostId(Long postId);

    List<Post> findByStatus(String accepted);

    List<Post> findByNumberOfAnsersEquals(int i);

    List<Post> findByCreatedAtGreaterThanOrderByCreatedAtDesc(Date createdAt);

    List<Post> findByStatusNotAndNumberOfAnsersNotOrderByCreatedAtDesc(String accepted, int i);

    List<Post> findByNumberOfAnsersGreaterThanOrStatus(int numberOfAnswers, String status);
}
