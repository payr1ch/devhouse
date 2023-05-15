package com.example.devhouse.starred;

import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StarredService {

    private final StarredRepo starredRepo;

    private final UserRepo userRepo;

    @Autowired
    public StarredService(StarredRepo starredRepo, UserRepo userRepo) {
        this.starredRepo = starredRepo;
        this.userRepo = userRepo;
    }

    public Starred save(Starred starred) {
        return starredRepo.save(starred);
    }

    public StarredDTO convertToStarredDTO(Starred starred) {
        StarredDTO starredDTO = new StarredDTO();
        starredDTO.setStarredId(starred.getStarredId());
        starredDTO.setPost(starred.getPost());
        starredDTO.setUser(starred.getUser());
        return starredDTO;
    }

    public List<Starred> getByUserId(User user) {
        return starredRepo.findStarredByUser(user);
    }

    public void remove(Long starredId) {
        starredRepo.deleteById(starredId);
    }



}