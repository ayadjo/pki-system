package com.example.demo.model;

import java.security.PublicKey;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

@Setter
@Getter
public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;

    public SubjectData() {

    }

    public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, Date startDate, Date endDate) {
        this.publicKey = publicKey;
        this.x500name = x500name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
