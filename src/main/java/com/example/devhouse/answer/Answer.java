package com.example.devhouse.answer;

import com.example.devhouse.post.Post;
import com.example.devhouse.user_things.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column()
    private String status = "NotAccepted";

    @Column()
    private Date createdAt;

    @Column()
    private int votes = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<UUID, Vote> votedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;


}
