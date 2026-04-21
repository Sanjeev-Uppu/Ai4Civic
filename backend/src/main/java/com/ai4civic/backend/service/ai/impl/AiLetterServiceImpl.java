package com.ai4civic.backend.service.ai.impl;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.service.ai.AiLetterService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Date;

@Service
public class AiLetterServiceImpl implements AiLetterService {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiLetterServiceImpl(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    @Override
    public String generateLetter(Complaint complaint) {

        String promptText = String.format("""
                You are an AI assistant that writes professional civic complaint letters.

                Citizen Information
                Name: %s
                Email: %s
                Location: %s
                Category: %s
                Priority: %s

                Complaint Description:
                %s

                Write a formal complaint letter addressed to the appropriate local authority.
                The letter must include:
                - Subject
                - Greeting
                - Problem explanation
                - Request for action
                - Closing with citizen details
                """,
                complaint.getName(),
                complaint.getEmail(),
                complaint.getLocation(),
                complaint.getCategory(),
                complaint.getPriority(),
                complaint.getDescription()
        );

        try {

            // ✅ Proper JSON creation (no manual escaping issues)
            String requestBody = mapper.writeValueAsString(
                    new java.util.HashMap<>() {{
                        put("contents", new Object[]{
                                new java.util.HashMap<>() {{
                                    put("parts", new Object[]{
                                            new java.util.HashMap<>() {{
                                                put("text", promptText);
                                            }}
                                    });
                                }}
                        });
                    }}
            );

            String response = webClient.post()
                    // ✅ FIXED MODEL
                    .uri("/v1/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            // ✅ Extract ONLY the generated letter
            JsonNode json = mapper.readTree(response);

            return json
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {

            System.err.println("Gemini API error: " + e.getMessage());
            return fallbackLetter(complaint);
        }
    }

    // ✅ Backup if API fails
    private String fallbackLetter(Complaint c) {

        return String.format("""
                Date: %tD

                Subject: Complaint regarding %s

                Dear Sir/Madam,

                I am writing to bring to your attention an issue in %s under the category of %s.

                %s

                Sincerely,
                %s
                (%s)
                """,
                new Date(),
                c.getCategory(),
                c.getLocation(),
                c.getCategory(),
                c.getDescription(),
                c.getName(),
                c.getEmail()
        );
    }
}