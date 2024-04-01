package com.example.demo.dto;

import com.example.demo.model.enumerations.ExtendedKey;
import com.example.demo.model.enumerations.KeyUsageExtension;
import lombok.Getter;
import lombok.Setter;
import com.example.demo.model.enumerations.CertificateType;

import java.util.Date;
import java.util.List;

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
    private List<KeyUsageExtension> keyUsageExtension;
    private List<ExtendedKey> extendedKey;
}
