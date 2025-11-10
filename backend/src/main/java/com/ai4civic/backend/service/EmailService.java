package com.ai4civic.backend.service;

import com.ai4civic.backend.entity.Complaint;

public interface EmailService {
    void sendComplaintEmails(Complaint complaint, String pdfPath);
}
