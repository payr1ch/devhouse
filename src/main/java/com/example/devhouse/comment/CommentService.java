package com.example.devhouse.comment;


import com.example.devhouse.answer.Answer;
import com.example.devhouse.answer.AnswerRepo;
import com.example.devhouse.user_things.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private UserRepo userRepo;


    public List<Comment> getCommentsByAnswerId(Long answerId) {
        Answer answer = answerRepo.findById(answerId).orElse(null);
        if (answer != null) {
            return commentRepo.findCommentsByAnswer(answer);
        }
        return null;
    }

    public Comment createComment(CreateCommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setComment(commentRequest.getComment());
        comment.setAuthor(userRepo.findUserByUserId(commentRequest.getUserId()));
        comment.setAnswer(answerRepo.findAnswerById(comment.getCommentId()));
        comment.setDate(new Date());
        return commentRepo.save(comment);
    }
}