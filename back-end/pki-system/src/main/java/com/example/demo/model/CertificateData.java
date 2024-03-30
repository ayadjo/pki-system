package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class CertificateData {

    @Id
    private String serialNumber;

    private Boolean revoked;

    public CertificateData(){

    }
}
