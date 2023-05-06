package com.example.devhouse.user_things.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/allUsers")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.findByUserId(id);
    }

    @PostMapping("/update/{id}")
    public User updateUser(@PathVariable UUID id, @ModelAttribute UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }
}
