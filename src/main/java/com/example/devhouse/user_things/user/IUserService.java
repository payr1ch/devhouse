package com.example.devhouse.user_things.user;

import com.example.devhouse.user_things.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    User findByUserId(UUID userid);
}
