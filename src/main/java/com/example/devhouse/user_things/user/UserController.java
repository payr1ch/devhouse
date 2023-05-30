package com.example.devhouse.user_things.user;

import com.example.devhouse.answer.Answer;
import com.example.devhouse.answer.AnswerService;
import com.example.devhouse.post.Post;
import com.example.devhouse.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final PostService postService;

    private final AnswerService answerService;

    @GetMapping("/allUsers")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable UUID id) {
        return userService.findByUserData(id);
    }

    @PostMapping("/update/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @GetMapping("/rank")
    public List<User> getUsersByRank() {
        return userService.getUsersByRank();
    }

    @GetMapping("/myPosts/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable UUID userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/myAnswers/{userId}")
    public ResponseEntity<List<Answer>> getAnswersByUserId(@PathVariable UUID userId) {
        List<Answer> answers = answerService.getAnswersByUserId(userId);
        return ResponseEntity.ok(answers);
    }

}
