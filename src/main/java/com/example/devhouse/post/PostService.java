package com.example.devhouse.post;

import com.example.devhouse.tag.Tag;
import com.example.devhouse.tag.TagRepo;
import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepository;

    @Autowired
    private TagRepo tagRepo;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(CreatePostRequest postRequest) throws IOException {
        Post post = new Post();

        post.setTitle(postRequest.getTitle());
        post.setAuthorId(postRequest.getAuthorId());
        String requestTagName = postRequest.getTagName();
        if (requestTagName != null) {
            Tag tag = tagRepo.findByNameIgnoreCase(requestTagName);
            if (tag != null) {
                tag.setNumberOfPosts(tag.getNumberOfPosts() + 1);
                post.setTag(tag);
            } else {
                Tag newTag = new Tag();
                newTag.setNumberOfPosts(1);
                newTag.setName(requestTagName);
                tagRepo.save(newTag);
                post.setTag(newTag);
            }
        }

        post.setCreatedAt(new Date());

        List<Map<String, Object>> contentList = postRequest.getContent();
            ObjectMapper objectMapper = new ObjectMapper();
            String contentString = objectMapper.writeValueAsString(contentList);
            post.setContent(contentString);
        return postRepository.save(post);

}
    public List<Post> getPostsByUnanswered() {
        return postRepository.findByNumberOfAnsersEquals(0);
    }


    public List<Post> getRecentPosts() {
        Calendar oneDayAgo = Calendar.getInstance();
        oneDayAgo.add(Calendar.DAY_OF_YEAR, -1);
        return postRepository.findByCreatedAtGreaterThanOrderByCreatedAtDesc(oneDayAgo.getTime());
    }

    public List<Post> getUnacceptedPosts() {
        return postRepository.findByStatusNotAndNumberOfAnsersNotOrderByCreatedAtDesc("Unanswered", 0);
    }

    public List<Post> getTopPosts(){
        return postRepository.findByNumberOfAnsersGreaterThanOrStatus(5, "Accepted");
    }


    public List<Post> getPostsByUserId(UUID userId) {
        return postRepository.findByAuthorId(userId);
    }

    public List<Post> getPostsByTag(String tagName) {
        Tag tag = tagRepo.findByNameIgnoreCase(tagName);
        return postRepository.findByTag(tag);
    }
}