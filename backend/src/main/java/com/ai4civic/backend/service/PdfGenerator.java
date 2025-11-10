package com.ai4civic.backend.service;

import com.ai4civic.backend.entity.Complaint;

public interface PdfGenerator {
    byte[] generatePdf(Complaint complaint) throws Exception;
}
