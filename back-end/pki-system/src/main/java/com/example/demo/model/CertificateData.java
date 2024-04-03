package com.example.demo.model;

import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.model.enumerations.ExtendedKey;
import com.example.demo.model.enumerations.KeyUsageExtension;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
//Za prikaz u tabeli sertifikata
import java.util.List;

@Getter
@Setter
@Entity
public class CertificateData {

    @Id
    private String serialNumber;

    private Boolean revoked;

    private CertificateType certificateType;

    private String issuerMail;

    private String subjectMail;

    private Date startDate;
    private Date endDate;

    @ElementCollection(targetClass = KeyUsageExtension.class)
    @Enumerated(EnumType.STRING)
    private List<KeyUsageExtension> keyUsages;

    @ElementCollection(targetClass = ExtendedKey.class)
    @Enumerated(EnumType.STRING)
    private List<ExtendedKey> extendedKeyUsages;

    public CertificateData(){

    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }
}
