package com.ai4civic.backend.controller;

import com.ai4civic.backend.entity.Complaint;
import com.ai4civic.backend.service.ComplaintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Complaint> createComplaint(
            @RequestPart("complaint") String complaintJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        // Convert JSON string -> Complaint object
        ObjectMapper mapper = new ObjectMapper();
        Complaint complaint = mapper.readValue(complaintJson, Complaint.class);

        // Save complaint + image
        Complaint saved = complaintService.saveComplaint(complaint, image);
        return ResponseEntity.ok(saved);
    }
}
