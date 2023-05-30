package com.example.devhouse.answer;

import com.example.devhouse.user_things.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<AnswerDTO>> getAnswersForPost(@PathVariable Long postId) {
        List<AnswerDTO> answers = answerService.getAnswersForPost(postId);
        return ResponseEntity.ok(answers);
    }
//
//    @GetMapping("/answer/{id}")
//    public Answer getAnswerById(@PathVariable("id") Long id) throws IOException {
//        return answerService.getAnswerById(id);
//    }

    @PostMapping("/createAnswer")
    public Answer createAnswer(@RequestBody CreateAnswerRequest answerRequest) throws IOException {
            return answerService.createAnswer(answerRequest);
    }

    @PutMapping("/correct/{answerId}")
    public ResponseEntity<?> setAnswerAsCorrect(@PathVariable Long answerId, @RequestParam UUID userId) {
        try {
            answerService.setAnswerAsCorrect(answerId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upvote/{answerId}")
    public String upvoteAnswer(@PathVariable Long answerId, @RequestParam UUID userId) {
        try {
            return answerService.upvoteAnswer(answerId, userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build().toString();
        }
    }

    @PostMapping("/downvote/{answerId}")
    public String downvoteAnswer(@PathVariable Long answerId, @RequestParam UUID userId) {
        try {
            return answerService.downvoteAnswer(answerId, userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build().toString();
        }
    }

}
