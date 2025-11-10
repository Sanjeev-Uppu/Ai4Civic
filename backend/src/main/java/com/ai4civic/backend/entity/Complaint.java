package com.ai4civic.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ User details
    private String name;
    private String email;

    // ✅ Complaint details
    @Column(length = 1000)
    private String description;
    private String location;
    private String category;
    private String priority;

    // ✅ Generated & stored files
    private String imagePath;
    @Column(length = 5000)
    private String letterText;
    private String pdfPath;

    // ✅ Status
    private String status = "Received";

    // ✅ Manual getters & setters (safety for Lombok plugin issues)
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getImagePath() { return imagePath; }
    public String getLetterText() { return letterText; }
    public String getPdfPath() { return pdfPath; }
    public String getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setLetterText(String letterText) { this.letterText = letterText; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public void setStatus(String status) { this.status = status; }
}
