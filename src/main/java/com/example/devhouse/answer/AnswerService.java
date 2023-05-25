package com.example.devhouse.answer;

import com.example.devhouse.notification.NotificationService;
import com.example.devhouse.post.Post;
import com.example.devhouse.post.PostRepo;
import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import com.fasterxml.jackson.core.JsonGenerator;
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

    @Autowired
    private NotificationService notificationService;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public List<AnswerDTO> getAnswersForPost(Long postId) {
        Post post = postRepo.findPostByPostId(postId);
        List<Answer> answers = answerRepo.findByPostOrderByVotesDesc(post);

        List<AnswerDTO> answerDTOs = new ArrayList<>();
        for (Answer answer : answers) {
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setId(answer.getId());
            answerDTO.setTitle(answer.getTitle());
            answerDTO.setVotes(answer.getVotes());
            answerDTO.setContent(answer.getContent());
            answerDTO.setStatus(answer.getStatus());
            answerDTO.setCreatedAt(answer.getCreatedAt());
            answerDTO.setVotedBy(answer.getVotedBy());
            answerDTO.setAuthor(answer.getAuthor());
            answerDTOs.add(answerDTO);
        }

        return answerDTOs;
    }

    public Answer createAnswer(CreateAnswerRequest answerRequest) throws IOException {
        Answer answer = new Answer();
        Post post = postRepo.findPostByPostId(answerRequest.getPostId());
        post.setNumberOfAnsers(post.getNumberOfAnsers() + 1);
        postRepo.save(post);
        answer.setTitle(answerRequest.getTitle());
        answer.setAuthor(userRepo.findUserByUserId(answerRequest.getAuthorId()));
        answer.setPost(postRepo.findPostByPostId(answerRequest.getPostId()));
        answer.setCreatedAt(new Date());
        List<Map<String, Object>> contentList = answerRequest.getContent();
        ObjectMapper objectMapper = new ObjectMapper();
        String contentString = objectMapper.writeValueAsString(contentList);
        answer.setContent(contentString);

        // Create notification for the post's author
        String title = "New Answer";
        String description = "A new answer has been posted on your post.";
        notificationService.createNotification(title, description, userRepo.findUserByUserId(post.getAuthorId()));
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
                String title = "Your answer was accepted";
                String description = "Your answer was helpfull! Well Done :)";
                notificationService.createNotification(title, description, userRepo.findUserByUserId(post.getAuthorId()));
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
            Post post = answer.getPost();
            String title = "You're earning points!";
            String description = "Your answer was upvoted. My boy ;)";
            notificationService.createNotification(title, description, user);

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
            Post post = answer.getPost();
            String title = "You're losing points!";
            String description = "Your answer was downvoted ((";
            notificationService.createNotification(title, description, user);
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

    public Answer getAnswerById(Long id) {
        return answerRepo.findAnswerById(id);
    }

    public List<Answer> getAnswersByUserId(UUID userId) {
        User user = userRepo.findUserByUserId(userId);
        return answerRepo.findByAuthor(user);
    }

}