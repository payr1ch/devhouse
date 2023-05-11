package com.example.devhouse.comment;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateCommentRequest {
    private UUID userId;

    private Long answerId;
    private String comment;
}
