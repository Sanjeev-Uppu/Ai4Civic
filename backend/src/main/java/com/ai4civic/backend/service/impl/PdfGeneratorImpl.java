package com.ai4civic.backend.service.impl;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.service.PdfGenerator;
import com.ai4civic.backend.util.ImageUtil;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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

            float margin = 50;
            float y = 750;
            float leading = 16;

            float pageWidth = page.getMediaBox().getWidth();
            float usableWidth = pageWidth - 2 * margin;

            // ===== TITLE =====
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
            cs.newLineAtOffset(margin, y);
            cs.showText("OFFICIAL CIVIC COMPLAINT");
            cs.endText();

            y -= 40;

            // ===== BODY =====
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.setLeading(leading);
            cs.newLineAtOffset(margin, y);

            String fullText = complaint.getLetterText();

            String[] paragraphs = fullText.split("\n");

            for (String para : paragraphs) {
                addWrappedText(cs, para, usableWidth, leading);
                cs.newLine();
            }

            cs.endText();

            // ===== IMAGE =====
            if (complaint.getImagePath() != null) {

                File file = new File(complaint.getImagePath());

                if (file.exists()) {

                    File safeImage = ImageUtil.convertToSafeImage(file);

                    if (safeImage != null) {

                        PDImageXObject image = PDImageXObject.createFromFile(
                                safeImage.getAbsolutePath(),
                                doc
                        );

                        float imgWidth = 200;
                        float imgHeight = 150;

                        float x = (pageWidth - imgWidth) / 2;
                        float yPos = 120;

                        cs.drawImage(image, x, yPos, imgWidth, imgHeight);
                    }
                }
            }

            cs.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);

            return baos.toByteArray();
        }
    }

    // ===== TEXT WRAP =====
    private void addWrappedText(PDPageContentStream cs,
                                String text,
                                float maxWidth,
                                float leading) throws Exception {

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {

            String testLine = line + word + " ";
            float fontSize = 12;

            float width = PDType1Font.HELVETICA.getStringWidth(testLine) / 1000 * fontSize;

            if (width > maxWidth) {
                cs.showText(line.toString());
                cs.newLineAtOffset(0, -leading);
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }

        if (!line.isEmpty()) {
            cs.showText(line.toString());
        }
    }
}