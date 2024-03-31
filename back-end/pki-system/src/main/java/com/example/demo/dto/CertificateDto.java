package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.model.enumerations.CertificateType;

import java.util.Date;

@Getter
@Setter
public class CertificateDto {
//dto za ca i ee
    private String issuerMail;
    private String subjectMail;
    private String issuerCertificateSerialNumber;
    private CertificateType issuerCertificateType;
    private CertificateType subjectCertificateType;
    private Date startDate;
    private Date endDate;
}
