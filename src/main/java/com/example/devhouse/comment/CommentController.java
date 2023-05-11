package com.example.devhouse.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/comment/{answerId}")
    public ResponseEntity<List<Comment>> getCommentsByAnswerId(@PathVariable Long answerId) {
        List<Comment> comments = commentService.getCommentsByAnswerId(answerId);
        return ResponseEntity.ok(comments);
    }
}

