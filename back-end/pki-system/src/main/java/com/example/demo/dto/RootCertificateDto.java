package com.example.demo.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootCertificateDto {
    private String issuerMail;
    private Date startDate;
    private Date endDate;

    public RootCertificateDto() {
    }

    public RootCertificateDto(String issuerMail, Date startDate, Date endDate) {
        this.issuerMail = issuerMail;
        this.startDate = startDate;
        this.endDate = endDate;
    }




}
