package com.example.devhouse.user_things.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String nickname);

    User findUserByUserId(UUID userId);

    List<User> findAllByOrderByRankDesc();
}
