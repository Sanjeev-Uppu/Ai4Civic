package com.ai4civic.backend.service.impl;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.repository.ComplaintRepository;
import com.ai4civic.backend.service.ComplaintService;
import com.ai4civic.backend.service.EmailService;
import com.ai4civic.backend.service.PdfGenerator;
import com.ai4civic.backend.service.ai.AiLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintRepository repo;

    @Autowired
    private AiLetterService aiLetterService;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private EmailService emailService;

    @Override
    public Complaint saveComplaint(Complaint complaint, MultipartFile image) throws Exception {

        // ✅ Create upload folder
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        // ✅ Save image (if uploaded)
        if (image != null && !image.isEmpty()) {
            String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String imagePath = uploadDir + File.separator + imageName;
            image.transferTo(new File(imagePath));
            complaint.setImagePath(imagePath);
        }

        // ✅ Generate AI-based letter
        String letterText = aiLetterService.generateLetter(complaint);
        complaint.setLetterText(letterText);

        // ✅ Generate PDF
        byte[] pdfBytes = pdfGenerator.generatePdf(complaint);
        String pdfDir = System.getProperty("user.dir") + File.separator + "letters";
        File pdfFolder = new File(pdfDir);
        if (!pdfFolder.exists()) pdfFolder.mkdirs();

        String pdfPath = pdfDir + File.separator + "complaint_" + System.currentTimeMillis() + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(pdfPath)) {
            fos.write(pdfBytes);
        }
        complaint.setPdfPath(pdfPath);

        // ✅ Save complaint in DB
        Complaint savedComplaint = repo.save(complaint);

        // ✅ Send emails to both user & collector
        emailService.sendComplaintEmails(savedComplaint, pdfPath);

        return savedComplaint;
    }
}
