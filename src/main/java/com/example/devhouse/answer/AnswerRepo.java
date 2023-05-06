package com.example.devhouse.answer;

import com.example.devhouse.post.Post;
import com.example.devhouse.user_things.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepo extends JpaRepository<Answer, Long> {


    List<Answer> findByPostOrderByVotesDesc(Post post);

    List<Answer> findByAuthor(User user);
}
