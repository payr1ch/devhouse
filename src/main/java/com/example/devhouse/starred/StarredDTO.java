package com.example.devhouse.starred;

import com.example.devhouse.post.Post;
import com.example.devhouse.user_things.user.User;
import lombok.Data;

@Data
public class StarredDTO {
    private Long starredId;
    private User user;
    private Post post;
}

