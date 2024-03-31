package com.example.demo.model;

import java.security.PublicKey;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.KeyPurposeId;

@Setter
@Getter
public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;
    private Integer[] keyUsage;
    private KeyPurposeId[] extendedKeyUsage;

    public SubjectData() {

    }

    public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, Date startDate, Date endDate, Integer[] keyUsage, KeyPurposeId[] extendedKeyUsage) {
        this.publicKey = publicKey;
        this.x500name = x500name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public Integer combineKeyUsageValues(){
        Integer combinedKeyUsage=0;
        for (int i=0;i<keyUsage.length;i++){
            combinedKeyUsage=combinedKeyUsage|keyUsage[i];
        }
        return combinedKeyUsage;
    }

}
