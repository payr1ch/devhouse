package com.example.devhouse.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/allPosts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> postOptional = postService.getById(id);
        return postOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest postRequest) {
        try {
            Post createdPost = postService.createPost(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/unanswered")
    public ResponseEntity<List<Post>> getPostsByUnanswered() {
        List<Post> posts = postService.getPostsByUnanswered();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Post>> getRecentPosts() {
        List<Post> posts = postService.getRecentPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/unaccepted")
    public ResponseEntity<List<Post>> getUnacceptedPosts() {
        List<Post> posts = postService.getUnacceptedPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/top")
    public ResponseEntity<List<Post>> getTopPosts() {
        List<Post> posts = postService.getTopPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Post>> getPostsByTag(@PathVariable String tag) {
        List<Post> posts = postService.getPostsByTag(tag);
        return ResponseEntity.ok(posts);
    }
}

