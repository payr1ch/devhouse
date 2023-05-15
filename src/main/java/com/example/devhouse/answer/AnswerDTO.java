package com.example.devhouse.answer;

import com.example.devhouse.user_things.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
public class AnswerDTO {
    private Long id;
    private String title;
    private int votes;
    private String content;
    private String status;
    private Date createdAt;
    private Map<UUID, Vote> votedBy;
    private User author;

}
