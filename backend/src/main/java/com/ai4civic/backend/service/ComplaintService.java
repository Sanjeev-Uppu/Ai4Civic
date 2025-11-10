package com.ai4civic.backend.service;

import com.ai4civic.backend.entity.Complaint;
import org.springframework.web.multipart.MultipartFile;

public interface ComplaintService {
    Complaint saveComplaint(Complaint complaint, MultipartFile image) throws Exception;
}
