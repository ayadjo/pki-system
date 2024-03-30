package com.example.demo.service;

import com.example.demo.dto.RootCertificateDto;
import com.example.demo.keystores.KeyStoreWriter;
import com.example.demo.model.IssuerData;
import com.example.demo.model.KeyStoreAccess;
import com.example.demo.model.SubjectData;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.Certificate;
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
        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null, KeyStoreAccess.password.toCharArray());
        keyStoreWriter.write(certificate.getSerialNumber().toString(),keyPair.getPrivate(),KeyStoreAccess.password.toCharArray(),new Certificate[]{certificate});
        keyStoreWriter.saveKeyStore(certificate.getSerialNumber().toString() + ".jks", KeyStoreAccess.password.toCharArray());
    }
}
