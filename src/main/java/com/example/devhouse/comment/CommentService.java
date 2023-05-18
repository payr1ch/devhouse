package com.example.devhouse.comment;


import com.example.devhouse.answer.Answer;
import com.example.devhouse.answer.AnswerRepo;
import com.example.devhouse.user_things.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
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




    public Flux<List<CommentDTO>> streamCommentsByAnswerId(Long answerId) {
        Answer answer = answerRepo.findById(answerId).orElse(null);
        if (answer != null) {
            List<Comment> existingComments = commentRepo.findCommentsByAnswer(answer);

            Flux<List<Comment>> newCommentsFlux = Flux.interval(Duration.ofSeconds(1))
                    .flatMap(i -> {
                        List<Comment> comments = commentRepo.findCommentsByAnswer(answer);
                        return Flux.just(comments);
                    });

            return Flux.concat(Flux.just(existingComments), newCommentsFlux)
                    .distinct()
                    .map(this::convertToCommentDTOList);
        }
        return Flux.empty();
    }

    private List<CommentDTO> convertToCommentDTOList(List<Comment> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setComment(comment.getComment());
            commentDTO.setAuthor(comment.getAuthor());
            commentDTO.setDate(comment.getDate());
            commentDTOs.add(commentDTO);
        }
        return commentDTOs;
    }


    public List<CommentDTO> getCommentsByAnswerId(Long answerId) {
        Answer answer = answerRepo.findById(answerId).orElse(null);
        if (answer != null) {
            List<Comment> comments = commentRepo.findCommentsByAnswer(answer);
            return convertToCommentDTOList(comments);
        }
        return null;
    }
    public Comment createComment(CreateCommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setComment(commentRequest.getComment());
        comment.setAuthor(userRepo.findUserByUserId(commentRequest.getUserId()));
        comment.setAnswer(answerRepo.findAnswerById(commentRequest.getAnswerId()));
        comment.setDate(new Date());
        return commentRepo.save(comment);
    }
}