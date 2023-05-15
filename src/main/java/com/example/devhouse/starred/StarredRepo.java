package com.example.devhouse.starred;

import com.example.devhouse.user_things.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StarredRepo extends JpaRepository<Starred, Long> {
    List<Starred> findStarredByUser(User user);
}
