package com.example.devhouse.answer;

import com.example.devhouse.post.Post;
import com.example.devhouse.post.PostRepo;
import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<Answer> getAnswersForPost(Long postId) {
        Post post = postRepo.findPostByPostId(postId);
        return answerRepo.findByPostOrderByVotesDesc(post);
    }

    public Answer createAnswer(CreateAnswerRequest answerRequest) throws IOException {
        Answer answer = new Answer();
        Post post = postRepo.findPostByPostId(answerRequest.getPostId());
        post.setNumberOfAnsers(post.getNumberOfAnsers() + 1);
        answer.setTitle(answerRequest.getTitle());
        answer.setAuthor(userRepo.findUserByUserId(answerRequest.getAuthorId()));
        answer.setPost(postRepo.findPostByPostId(answerRequest.getPostId()));
        answer.setCreatedAt(new Date());


        List<Map<String, Object>> content = answerRequest.getContent();
        if (content != null && !content.isEmpty()) {
            List<Map<String, String>> newContent = new ArrayList<>();
            for (Map<String, Object> element : content) {
                Map<String, String> newElement = new HashMap<>();
                for (Map.Entry<String, Object> entry : element.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith("image")) {
                        // Convert image to base64
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
            answer.setContent(jsonContent);
        }
        postRepo.save(post);
        return answerRepo.save(answer);
    }


    public void setAnswerAsCorrect(Long answerId, UUID userId) {
        Answer answer = answerRepo.findById(answerId).orElse(null);
        if (answer != null) {
            Post post = answer.getPost();
            if (post.getAuthorId().equals(userId)) {
                answer.setStatus("Helpful");
                post.setStatus("Accepted");
                answerRepo.save(answer);
            }
        }
    }

    public void upvoteAnswer(Long answerId, UUID userId) {
        Answer answer = answerRepo.findById(answerId).orElse(null);
        User user;
        if (answer != null) {
            user = answer.getAuthor();
            Map<UUID, Vote> votedBy = answer.getVotedBy();
            if (votedBy.containsKey(userId)) {
                if (votedBy.get(userId) == Vote.DOWNVOTE) {
                    answer.setVotes(answer.getVotes() + 2);
                    votedBy.put(userId, Vote.UPVOTE);
                } else {
                    answer.setVotes(answer.getVotes() - 1);
                    votedBy.remove(userId);
                }
            } else {
                answer.setVotes(answer.getVotes() + 1);
                votedBy.put(userId, Vote.UPVOTE);
            }
            updateRankForUser(user.getUserId());
            answerRepo.save(answer);
        }
    }

    public void downvoteAnswer(Long answerId, UUID userId) {
        Answer answer = answerRepo.findById(answerId).orElse(null);
        User user;
        if (answer != null) {
            user = answer.getAuthor();
            Map<UUID, Vote> votedBy = answer.getVotedBy();
            if (votedBy.containsKey(userId)) {
                if (votedBy.get(userId) == Vote.UPVOTE) {
                    answer.setVotes(answer.getVotes() - 2);
                    votedBy.put(userId, Vote.DOWNVOTE);
                } else {
                    answer.setVotes(answer.getVotes() + 1);
                    votedBy.remove(userId);
                }
            } else {
                answer.setVotes(answer.getVotes() - 1);
                votedBy.put(userId, Vote.DOWNVOTE);
            }
            updateRankForUser(user.getUserId());
            answerRepo.save(answer);
        }
    }

    public void updateRankForUser(UUID userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            List<Answer> answers = answerRepo.findByAuthor(user);
            int totalVotes = answers.stream()
                    .mapToInt(Answer::getVotes)
                    .sum();
            user.setRank(totalVotes);
            userRepo.save(user);
        }
    }
}