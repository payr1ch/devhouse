package com.example.devhouse.user_things.user;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private String email;
    private String password;
    private String username;
    private String groups;
    private String ava;
    private int rank;
    private Boolean isEnabled;
    private int numberOfPosts;
    private int numberOfAnswers;
}
