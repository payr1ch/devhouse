package com.example.devhouse.search;

import com.example.devhouse.post.PostRepo;
import com.example.devhouse.post.Post;
import com.example.devhouse.tag.Tag;
import com.example.devhouse.tag.TagRepo;
import com.example.devhouse.user_things.user.UserRepo;
import com.example.devhouse.user_things.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private final UserRepo userRepository;
    @Autowired
    private final PostRepo postRepository;
    @Autowired
    private final TagRepo tagRepo;
    public SearchService(UserRepo userRepository, PostRepo postRepository, TagRepo tagRepo) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepo = tagRepo;
    }

    public Object search(String query) {
        if (query.startsWith("@")) {
            // Search for users
            String username = query.substring(1); // Remove the '@' symbol
            List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
            return users;
        } else if (query.startsWith("#")) {
            // Search for tags
            String tag = query.substring(1); // Remove the '#' symbol
            List<Tag> tags = tagRepo.findByNameContainingIgnoreCase(tag);
            return tags;
        } else {
            // Search for posts
            List<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
            return posts;
        }
    }
}
