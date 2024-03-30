package com.example.demo.service;

import com.example.demo.dto.RootCertificateDto;
import com.example.demo.keystores.KeyStoreWriter;
import com.example.demo.model.*;
import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.KeyStoreAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private KeyStoreAccessRepository keyStoreAccessRepository;

    @Autowired
    private CertificateRepository certificateRepository;
    public void createRootCertificate(RootCertificateDto root, String pass) {
        if (root == null || pass == null) {
            // Ovde možete dodati odgovarajući tretman za null vrednosti
            return;
        }

        User issuer = userService.getByUsername(root.getIssuerMail());
        // Provera da li je objekat issuer null
        if (issuer == null) {
            throw new IllegalArgumentException("Issuer cannot be null");
        }

        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        SubjectData subjectData = certificateGeneratorService.generateSubjectData(keyPair, issuer, root.getStartDate(), root.getEndDate());
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(keyPair.getPrivate(), issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        CertificateData cert = new CertificateData();
        cert.setSerialNumber(certificate.getSerialNumber().toString());
        cert.setRevoked(false);
        cert.setCertificateType(CertificateType.ROOT);
        cert.setIssuerMail(issuer.getMail());
        cert.setSubjectMail(issuer.getMail());
        cert.setStartDate(root.getStartDate());
        cert.setEndDate(root.getEndDate());
        certificateRepository.save(cert);

        String fileName = certificate.getSerialNumber().toString() + ".jks";
        String filePass = hashPassword(pass);

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null, filePass.toCharArray());
        keyStoreWriter.write(certificate.getSerialNumber().toString() + issuer.getMail(), keyPair.getPrivate(), filePass.toCharArray(), new Certificate[]{certificate});
        keyStoreWriter.saveKeyStore(fileName, filePass.toCharArray());

        // Store the fileName and hashed filePass
        KeyStoreAccess keyStoreAccess = new KeyStoreAccess();
        keyStoreAccess.setFileName(fileName);
        keyStoreAccess.setFilePass(filePass);
        keyStoreAccessRepository.save(keyStoreAccess); // Assuming you have a repository to save KeyStoreAccess

    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

}
