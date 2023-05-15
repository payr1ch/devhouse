package com.example.devhouse.starred;

import com.example.devhouse.post.Post;
import com.example.devhouse.user_things.user.User;
import jakarta.persistence.*;


@Entity
public class Starred {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long starredId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters, and setters

    public Starred() {
    }

    public Starred(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    // Getters and setters

    public Long getStarredId() {
        return starredId;
    }

    public void setStarredId(Long starredId) {
        this.starredId = starredId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
