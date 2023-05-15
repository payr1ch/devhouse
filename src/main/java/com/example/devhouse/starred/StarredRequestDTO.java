package com.example.devhouse.starred;

import lombok.Data;

import java.util.UUID;

@Data
public class StarredRequestDTO {
    private UUID userId;
    private Long postId;
}

