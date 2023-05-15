package com.example.devhouse.post;

import com.example.devhouse.tag.Tag;
import com.example.devhouse.tag.TagRepo;
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
            if (tag != null){
                tag.setNumberOfPosts(tag.getNumberOfPosts() + 1);
                post.setTag(tag);
            }else{
                Tag newTag = new Tag();
                newTag.setNumberOfPosts(1);
                newTag.setName(requestTagName);
                tagRepo.save(newTag);
                post.setTag(newTag);
            }
        }

        post.setCreatedAt(new Date());

        List<Map<String, Object>> content = postRequest.getContent();
        if (content != null && !content.isEmpty()) {
            List<Map<String, String>> newContent = new ArrayList<>();
            for (Map<String, Object> element : content) {
                Map<String, String> newElement = new HashMap<>();
                for (Map.Entry<String, Object> entry : element.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith("image")) {
                        // Convert image to base64\
                        MultipartFile value = (MultipartFile) entry.getValue();
                        String base64Image = Base64.getEncoder().encodeToString(value.getBytes());
                        newElement.put(key, base64Image);
                    } else {
                        String value = (String) entry.getValue();
                        newElement.put(key, value);
                    }
                }
                newContent.add(newElement);
            }
            String jsonContent = objectMapper.writeValueAsString(newContent);
            jsonContent = jsonContent.replaceAll("\\\\", "");
            post.setContent(jsonContent);
        }

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
        return postRepository.findByStatusNotAndNumberOfAnsersNotOrderByCreatedAtDesc("Accepted", 0);
    }

    public List<Post> getTopPosts(){
        return postRepository.findByNumberOfAnsersGreaterThanOrStatus(5, "Accepted");
    }
}