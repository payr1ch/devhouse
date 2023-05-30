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

    public Object search(SearchRequest request) {
        String query = request.getQuery();
        if (query.startsWith("@")) {
            String username = query.substring(1); // Remove the '@' symbol
            return userRepository.findByUsernameContainingIgnoreCase(username);
        } else if (query.startsWith("#")) {
            String tag = query.substring(1); // Remove the '#' symbol
            return tagRepo.findByNameContainingIgnoreCase(tag);
        } else {
            return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
        }
    }
}
