package com.ai4civic.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Value("${app.storage.upload-dir:uploads}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
}
