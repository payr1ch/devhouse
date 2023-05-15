package com.example.devhouse.summarize;

import lombok.Data;

@Data
class RequestBody {
    private String model;
    private String prompt;
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;

    public RequestBody(String model, String prompt, double temperature, int max_tokens, double top_p, double frequency_penalty, double presence_penalty) {
        this.model = model;
        this.prompt = prompt;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
        this.top_p = top_p;
        this.frequency_penalty = frequency_penalty;
        this.presence_penalty = presence_penalty;
    }

    // getters and setters
}