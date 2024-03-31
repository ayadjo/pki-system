package com.example.demo.dto;

import com.example.demo.model.CertificateData;
import com.example.demo.model.KeyStoreAccess;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyStoreDto {
    public String fileName;
    public String alias;
}
