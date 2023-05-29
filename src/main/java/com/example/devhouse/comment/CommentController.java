package com.example.devhouse.comment;

import com.example.devhouse.answer.AnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<Comment> createComment(@RequestBody CreateCommentRequest commentRequest) {
        Comment comment = commentService.createComment(commentRequest);
        return ResponseEntity.ok(comment);
    }

//    @GetMapping("/comment/{answerId}")
//    public ResponseEntity<List<CommentDTO>> getCommentsByAnswerId(@PathVariable Long answerId) {
//        List<CommentDTO> comments = commentService.getCommentsByAnswerId(answerId);
//        return ResponseEntity.ok(comments);
//    }
@GetMapping(value = "/commentByAnswer/{answerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<List<CommentDTO>> streamCommentsByAnswerId(@PathVariable Long answerId) {
    return commentService.streamCommentsByAnswerId(answerId);
}

    @GetMapping("/{answerId}")
    public ResponseEntity<List<CommentDTO>> getAnswersForPost(@PathVariable Long answerId) {
        List<CommentDTO> comments = commentService.getCommentsByAnswerId(answerId);
        return ResponseEntity.ok(comments);
    }
}

