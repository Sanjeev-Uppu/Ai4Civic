package com.ai4civic.backend.service.ai;

import com.ai4civic.backend.entity.Complaint;

public interface AiLetterService {
    /**
     * Generate a professional letter content from a Complaint.
     * Implementations may call an AI provider or use a template fallback.
     */
    String generateLetter(Complaint complaint) throws Exception;
}
