package com.example.devhouse.summarize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SummarizationService {
    @Autowired
    public JdbcTemplate jdbcTemplate;
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final String MODEL_NAME = "text-davinci-003";
    private static final double TEMPERATURE = 0.7;
    private static final int MAX_TOKENS = 500;
    private static final double TOP_P = 1.0;
    private static final double FREQUENCY_PENALTY = 0.0;
    private static final double PRESENCE_PENALTY = 0.0;

    // Method to generate a summary
    public String generateSummary(String title, String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        RestTemplate restTemplate = new RestTemplate();

        // Create the request body using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(
                    new RequestBody(MODEL_NAME, "Summarize as much as possible these texts and explain what does codes to user:\\n\\n" + title + " " + prompt, TEMPERATURE, MAX_TOKENS, TOP_P, FREQUENCY_PENALTY, PRESENCE_PENALTY)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately
        }

        // Create the HTTP entity
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Make the HTTP request
        String response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class).getBody();
        String summary = response;

        return summary;
    }
    public String extractTextsAndCodes(String jsonString) {
        StringBuilder result = new StringBuilder();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String text = json.optString("text");
                String code = json.optString("code");

                if (!code.isEmpty()) {
                    result.append(code).append(" ");
                }

                if (!text.isEmpty()) {
                    result.append(text).append(" ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString().trim();
    }
}
