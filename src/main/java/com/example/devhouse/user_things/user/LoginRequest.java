package com.example.devhouse.user_things.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoginRequest {

    private String username;
    private String password;
    // getters and setters

}