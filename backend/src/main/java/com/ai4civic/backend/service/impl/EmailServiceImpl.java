package com.ai4civic.backend.service.impl;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.collector}")
    private String collectorEmail;

    @Override
    public void sendComplaintEmails(Complaint complaint, String pdfPath) {
        sendUserConfirmation(complaint, pdfPath);
        sendCollectorNotification(complaint, pdfPath);
    }

    // ✅ User confirmation email
    private void sendUserConfirmation(Complaint complaint, String pdfPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(complaint.getEmail());
            helper.setSubject("✅ Complaint Submitted Successfully - " + complaint.getCategory());
            helper.setText("<html><body>" +
                    "<h3>Dear " + complaint.getName() + ",</h3>" +
                    "<p>Your complaint has been successfully submitted to the Ai4Civic platform.</p>" +
                    "<p><b>Category:</b> " + complaint.getCategory() + "<br>" +
                    "<b>Description:</b> " + complaint.getDescription() + "</p>" +
                    "<p>We’ve attached a professionally formatted complaint letter for your records.</p>" +
                    "<br><p>Thank you for helping improve your community.</p>" +
                    "<p>Regards,<br><b>Ai4Civic Support Team</b></p>" +
                    "</body></html>", true);

            // Attach the generated PDF
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                helper.addAttachment("Complaint_Letter.pdf", pdfFile);
            }

            // Attach image (if available)
            if (complaint.getImagePath() != null) {
                File img = new File(complaint.getImagePath());
                if (img.exists()) helper.addAttachment("Evidence_Image.jpg", img);
            }

            mailSender.send(message);
            System.out.println("📨 Sent confirmation email to user: " + complaint.getEmail());
        } catch (MessagingException e) {
            System.err.println("⚠️ Failed to send user confirmation: " + e.getMessage());
        }
    }

    // ✅ Official email to Collector
    private void sendCollectorNotification(Complaint complaint, String pdfPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(collectorEmail);
            helper.setSubject("🚨 Citizen Complaint Received - " + complaint.getCategory());
            helper.setText("<html><body>" +
                    "<h3>Dear District Collector,</h3>" +
                    "<p>Kindly review the following citizen complaint for necessary action:</p>" +
                    "<p><b>Complainant Name:</b> " + complaint.getName() + "<br>" +
                    "<b>Email:</b> " + complaint.getEmail() + "<br>" +
                    "<b>Location:</b> " + complaint.getLocation() + "<br>" +
                    "<b>Category:</b> " + complaint.getCategory() + "<br>" +
                    "<b>Priority:</b> " + complaint.getPriority() + "</p>" +
                    "<p><b>Description:</b> " + complaint.getDescription() + "</p>" +
                    "<p>The official complaint letter and image evidence are attached below.</p>" +
                    "<p>Thank you for your prompt attention.</p>" +
                    "<br><p>Respectfully,<br><b>Ai4Civic Complaint System</b></p>" +
                    "</body></html>", true);

            // Attach the PDF
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                helper.addAttachment("Citizen_Complaint.pdf", pdfFile);
            }

            // Attach image (if available)
            if (complaint.getImagePath() != null) {
                File img = new File(complaint.getImagePath());
                if (img.exists()) helper.addAttachment("Evidence_Image.jpg", img);
            }

            mailSender.send(message);
            System.out.println("📩 Sent official email to Collector: " + collectorEmail);
        } catch (MessagingException e) {
            System.err.println("⚠️ Failed to send collector email: " + e.getMessage());
        }
    }
}
