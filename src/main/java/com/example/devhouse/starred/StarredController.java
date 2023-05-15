package com.example.devhouse.starred;

import com.example.devhouse.post.PostRepo;
import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import com.example.devhouse.user_things.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.devhouse.post.Post;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/starred")
public class StarredController {

    private final StarredService starredService;

    private final UserRepo userRepo;

    private final UserService userService;

    private final PostRepo postRepo;
    @Autowired
    public StarredController(StarredService starredService, UserRepo userRepo, UserService userService, PostRepo postRepo) {
        this.starredService = starredService;
        this.userRepo = userRepo;
        this.userService = userService;
        this.postRepo = postRepo;
    }

    @PostMapping("/createStarred")
    public ResponseEntity<StarredDTO> saveStarredPost(@RequestBody StarredRequestDTO requestDTO) {
        User user = userService.findByUserId(requestDTO.getUserId());
        Post post = postRepo.findPostByPostId(requestDTO.getPostId());

        if (user == null || post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Starred starred = new Starred();
        starred.setUser(user);
        starred.setPost(post);

        Starred savedStarred = starredService.save(starred);
        StarredDTO responseDTO = starredService.convertToStarredDTO(savedStarred);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Starred>> getStarredPostsByUserId(@PathVariable UUID userId) {
        User user = userRepo.findUserByUserId(userId);
        List<Starred> starredPosts = starredService.getByUserId(user);
        return new ResponseEntity<>(starredPosts, HttpStatus.OK);
    }

    @DeleteMapping("/{starredId}")
    public ResponseEntity<Void> removeStarredPost(@PathVariable Long starredId) {
        starredService.remove(starredId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
