package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import com.example.demo.model.enumerations.ExtendedKey;
import com.example.demo.model.enumerations.KeyUsageExtension;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RootCertificateDto {
    private String issuerMail;
    private Date startDate;
    private Date endDate;
    private List<KeyUsageExtension> keyUsageExtension;
    private List<ExtendedKey> extendedKey;

    public RootCertificateDto() {
    }

    public RootCertificateDto(String issuerMail, Date startDate, Date endDate, List<KeyUsageExtension> keyUsageExtension, List<ExtendedKey> extendedKey) {
        this.issuerMail = issuerMail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.keyUsageExtension = keyUsageExtension;
        this.extendedKey = extendedKey;
    }
}
