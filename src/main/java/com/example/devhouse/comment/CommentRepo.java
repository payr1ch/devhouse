package com.example.devhouse.comment;

import com.example.devhouse.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByAnswer(Answer answer);

    List<Comment> findCommentsByAnswerAndDateGreaterThan(Answer answer, Date lastCommentDate);
}
