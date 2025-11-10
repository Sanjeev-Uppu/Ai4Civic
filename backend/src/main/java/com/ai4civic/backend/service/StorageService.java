package com.ai4civic.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * Stores the multipart file and returns the file system path
     */
    String store(MultipartFile file) throws Exception;
}
