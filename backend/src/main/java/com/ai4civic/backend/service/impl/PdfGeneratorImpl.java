package com.ai4civic.backend.service.impl;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.service.PdfGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class PdfGeneratorImpl implements PdfGenerator {

    @Override
    public byte[] generatePdf(Complaint complaint) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(doc, page);

            // Title
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
            cs.newLineAtOffset(50, 750);
            cs.showText("Official Complaint Letter");
            cs.endText();

            // Letter body
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(50, 720);

            String[] lines = complaint.getLetterText().split("\n");
            float leading = 14f;
            for (String line : lines) {
                cs.showText(line);
                cs.newLineAtOffset(0, -leading);
            }
            cs.endText();

            // If image exists, draw it (scaled)
            if (complaint.getImagePath() != null) {
                File f = new File(complaint.getImagePath());
                if (f.exists()) {
                    PDImageXObject pdImage = PDImageXObject.createFromFileByContent(f, doc);
                    float scale = 1f;
                    cs.drawImage(pdImage, 50, 200, pdImage.getWidth() * scale / 3, pdImage.getHeight() * scale / 3);
                }
            }

            cs.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }
}
