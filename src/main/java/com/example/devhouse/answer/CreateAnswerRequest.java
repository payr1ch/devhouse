package com.example.devhouse.answer;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Data
public class CreateAnswerRequest {
    private String title;
    private List<Map<String, Object>> content;
    private Long postId;
    private UUID authorId;
}
