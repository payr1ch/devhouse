package com.example.devhouse.summarize;
import com.example.devhouse.answer.AnswerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummarizationController {

    private final SummarizationService summarizationService;

    private final AnswerRepo answerRepo;
    @Autowired
    public SummarizationController(SummarizationService summarizationService, AnswerRepo answerRepo) {
        this.summarizationService = summarizationService;
        this.answerRepo = answerRepo;
    }

    @PostMapping("/{id}")
    public String generateSummary(@PathVariable Long id) {

        String title = answerRepo.findAnswerById(id).getTitle();
        String content = answerRepo.findAnswerById(id).getContent();
        String extractedContent = summarizationService.extractTextsAndCodes(content);

        return summarizationService.generateSummary(title, extractedContent);
    }


}