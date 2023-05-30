package com.example.devhouse.user_things.user;

import com.example.devhouse.user_things.registration.RegistrationRequest;
import com.example.devhouse.user_things.registration.token.VerificationToken;
import com.example.devhouse.user_things.registration.token.VerificationTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.devhouse.user_things.exception.UserAlreadyExistsException;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepo verificationTokenRepo;
    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public List<User> getUsersByRank() {
        return userRepo.findAllByOrderByRankDesc();
    }
    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        Optional<User> user2 = this.userRepo.findByUsername(request.username());
        if(user.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with email " +  request.email() + " already exists");
        }
        if(user2.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with username " +  request.username() + " already exists");
        }
        var newUser = new User();
        newUser.setEmail(request.email());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setGroups("");
        newUser.setRank(0);
        newUser.setAva(null);
        newUser.setRole("USER");
        return userRepo.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
     var verificationToken = new VerificationToken(token, theUser);
     verificationTokenRepo.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = verificationTokenRepo.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepo.delete(token);
            return "Token already expired";
        }
        user.setIsEnabled(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public User findByUserId(UUID userid) {
        return userRepo.findUserByUserId(userid);
    }

    public User updateUser(UUID userId, UserUpdateRequest updateRequest) {
        User user = userRepo.findUserByUserId(userId);

        if (!updateRequest.getUsername().isEmpty()) {
            User user2 = userRepo.findUserByUsername(updateRequest.getUsername());
            if(user2 == null){
                user.setUsername(updateRequest.getUsername());
            }

        }

        if (updateRequest.getAva() != null) {
            user.setAva(updateRequest.getAva());
        }

        if (updateRequest.getGroups() != null) {
            user.setGroups(updateRequest.getGroups());
        }

        return userRepo.save(user);
    }



}
