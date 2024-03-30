package com.example.demo.model;

import com.example.demo.model.enumerations.CertificateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

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

    public CertificateData(){

    }
}
