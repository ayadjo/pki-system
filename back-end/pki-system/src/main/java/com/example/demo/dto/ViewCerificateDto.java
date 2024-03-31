package com.example.demo.dto;

import com.example.demo.model.enumerations.CertificateType;
import lombok.Getter;
import lombok.Setter;

import java.security.cert.X509Certificate;
import java.util.Date;

//Za prikaz pojedinacnog sertifikata
@Getter
@Setter
public class ViewCerificateDto {
    private UserDataDto subjectData;
    private UserDataDto issuerData;
    private String serialNumber;
    private Date startDate;
    private Date endDate;
    //private List<KeyUsage> keyUsages;
    //private List<ExtendedKeyUsage> extendedKeyUsages;
    private CertificateType certificateType;
    private boolean revoked;

    public ViewCerificateDto(X509Certificate certificate, CertificateType certificateType, boolean revoked) {
        this.subjectData = new UserDataDto(certificate.getSubjectX500Principal().getName());
        this.issuerData = new UserDataDto(certificate.getIssuerX500Principal().getName());
        this.serialNumber = certificate.getSerialNumber().toString();
        this.startDate = certificate.getNotBefore();
        this.endDate = certificate.getNotAfter();
        //this.keyUsages = keyUsages;
        //this.extendedKeyUsages = extendedKeyUsages;
        this.revoked = revoked;
        this.certificateType = certificateType;

    }
}
