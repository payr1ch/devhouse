package com.example.devhouse.user_things.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {

    private String username;
    private String groups;
    private MultipartFile ava;

    // getters and setters

}