package com.example.devhouse.comment;

import com.example.devhouse.user_things.user.User;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private Long commentId;
    private String comment;
    private User author;
    private Date date;
}
