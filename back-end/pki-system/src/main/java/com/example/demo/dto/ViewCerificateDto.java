package com.example.demo.dto;

import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.model.enumerations.KeyUsageExtension;
import lombok.Getter;
import lombok.Setter;

import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//Za prikaz pojedinacnog sertifikata
@Getter
@Setter
public class ViewCerificateDto {
    private UserDataDto subjectData;
    private UserDataDto issuerData;
    private String serialNumber;
    private Date startDate;
    private Date endDate;
    private List<KeyUsageExtension> keyUsages;
    private CertificateType certificateType;
    private boolean revoked;

    public ViewCerificateDto(X509Certificate certificate, CertificateType certificateType, boolean revoked, List<KeyUsageExtension> keyUsageExtension) {
        this.subjectData = new UserDataDto(certificate.getSubjectX500Principal().getName());
        this.issuerData = new UserDataDto(certificate.getIssuerX500Principal().getName());
        this.serialNumber = certificate.getSerialNumber().toString();
        this.startDate = certificate.getNotBefore();
        this.endDate = certificate.getNotAfter();
        this.keyUsages = keyUsageExtension;
        this.revoked = revoked;
        this.certificateType = certificateType;

    }
}
