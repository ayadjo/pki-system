package com.example.demo.repository;

import com.example.demo.model.CertificateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository  extends JpaRepository<CertificateData, String> {
}
