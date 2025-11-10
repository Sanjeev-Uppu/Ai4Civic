package com.ai4civic.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ai4civic.backend.entity.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {}
