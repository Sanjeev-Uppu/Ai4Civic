package com.ai4civic.backend.service.ai.impl;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.service.ai.AiLetterService;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.nio.file.Files;

@Service
public class AiLetterServiceImpl implements AiLetterService {

    @Autowired
    private OpenAiChatModel chatModel;

    @Override
    public String generateLetter(Complaint complaint) {
        String imageContext = "";

        try {
            if (complaint.getImagePath() != null) {
                File imageFile = new File(complaint.getImagePath());
                if (imageFile.exists()) {
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                    String base64 = Base64Utils.encodeToString(imageBytes);

                    String visionPrompt = """
                            Analyze the image provided (base64 format). 
                            Describe what civic issue or environmental situation it likely represents 
                            in 1–2 lines of plain English (e.g., waterlogging, pollution, damaged roads, waste, etc.).
                            """;

                    String fullPrompt = visionPrompt + "\nBase64 Image: " + base64;

                    // Send image context to GPT-4o Vision
                    Prompt prompt = new Prompt(new UserMessage(fullPrompt));
                    String visionDescription = chatModel.call(prompt).getResult().getOutput().getContent();

                    imageContext = "\n\nAI Image Analysis:\n" + visionDescription.trim();
                }
            }
        } catch (Exception e) {
            imageContext = "\n\n(Note: Image could not be analyzed automatically.)";
            System.err.println("⚠️ AI Vision error: " + e.getMessage());
        }

        // Now generate the main letter
        String promptText = String.format("""
                You are an AI that writes formal complaint letters for civic issues.

                User Details:
                - Name: %s
                - Email: %s
                - Location: %s
                - Category: %s
                - Priority: %s

                Complaint Description:
                %s

                Additional Context:
                %s

                Write a complete formal letter addressed to the local authorities with:
                - A professional subject
                - Polite tone
                - Problem description
                - A call to action
                - Proper closing and signature.
                """,
                complaint.getName(),
                complaint.getEmail(),
                complaint.getLocation(),
                complaint.getCategory(),
                complaint.getPriority(),
                complaint.getDescription(),
                imageContext
        );

        try {
            Prompt finalPrompt = new Prompt(new UserMessage(promptText));
            return chatModel.call(finalPrompt).getResult().getOutput().getContent();

        } catch (Exception e) {
            System.err.println("⚠️ Letter generation fallback due to error: " + e.getMessage());
            return fallbackLetter(complaint);
        }
    }

    private String fallbackLetter(Complaint c) {
        return String.format("""
                Date: %tD

                To Whom It May Concern,

                Subject: Complaint regarding %s

                Dear Sir/Madam,

                I am writing to bring to your attention an issue faced in %s under the category of %s.
                %s

                Kindly take the necessary actions to resolve this issue at the earliest.

                Sincerely,
                %s
                (%s)
                """, new java.util.Date(),
                c.getDescription(),
                c.getLocation(),
                c.getCategory(),
                c.getDescription(),
                c.getName(),
                c.getEmail());
    }
}
