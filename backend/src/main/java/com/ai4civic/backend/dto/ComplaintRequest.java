package com.ai4civic.backend.dto;

import org.springframework.web.multipart.MultipartFile;

public class ComplaintRequest {
    private String name;
    private String email;
    private String description;
    private MultipartFile image;

    public ComplaintRequest() {}

    public ComplaintRequest(String name, String email, String description, MultipartFile image) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.image = image;
    }

    // getters & setters
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }

    public MultipartFile getImage(){ return image; }
    public void setImage(MultipartFile image){ this.image = image; }
}
