package com.example.devhouse.post;


import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class CreatePostRequest {
    private String title;
    private List<Map<String, Object>> content;
    private String tagName;
    private UUID authorId;
}
