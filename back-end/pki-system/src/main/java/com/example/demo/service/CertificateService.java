package com.example.demo.service;

import com.example.demo.dto.RootCertificateDto;
import com.example.demo.model.IssuerData;
import com.example.demo.model.SubjectData;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.X509Certificate;


@Service
public class CertificateService {

    @Autowired
    private UserService userService;

    @Autowired
    private CertificateGeneratorService certificateGeneratorService;
    public void createRootCertificate(RootCertificateDto root)
    {
        User issuer = userService.getByUsername(root.getIssuerMail());
        KeyPair keyPair= certificateGeneratorService.generateKeyPair();
        SubjectData subjectData= certificateGeneratorService.generateSubjectData(keyPair, issuer, root.getStartDate(), root.getEndDate());
        IssuerData issuerData= certificateGeneratorService.generateIssuerData(keyPair.getPrivate(), issuer);
        X509Certificate certificate= certificateGeneratorService.generateCertificate(subjectData,issuerData);

        //KeyStore
    }
}
