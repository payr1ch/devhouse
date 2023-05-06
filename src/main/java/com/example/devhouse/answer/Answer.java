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
    private String status = "NotAcceptedYet";

    @Column()
    private Date createdAt;

    @Column()
    private int votes = 0;

    @ElementCollection
    private Map<UUID, Vote> votedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question")
    private Post post;


}
