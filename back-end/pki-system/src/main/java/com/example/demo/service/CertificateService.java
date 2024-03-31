package com.example.demo.service;

import com.example.demo.converter.ExtendedKeyConverter;
import com.example.demo.converter.KeyUsageExtensionConverter;
import com.example.demo.dto.RootCertificateDto;
import com.example.demo.keystores.KeyStoreWriter;
import com.example.demo.model.*;
import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.KeyStoreAccessRepository;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.enumerations.KeyUsageExtension;
import com.example.demo.model.enumerations.ExtendedKey;


import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    private final KeyUsageExtensionConverter keyUsageExtensionConverter;
    private final ExtendedKeyConverter extendedKeyConverter;

    public CertificateService(KeyUsageExtensionConverter keyUsageConverter, ExtendedKeyConverter extendedKeyUsageConverter) {
        this.keyUsageExtensionConverter = keyUsageConverter;
        this.extendedKeyConverter = extendedKeyUsageConverter;
    }

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

        Integer[] keyUsages = keyUsageExtensionConverter.convertKeyUsageToInteger(root.getKeyUsageExtension());
        KeyPurposeId[] extendedKeyUsages = extendedKeyConverter.convertToExtendedKey(root.getExtendedKey());
        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        SubjectData subjectData = certificateGeneratorService.generateSubjectData(keyPair, issuer, root.getStartDate(), root.getEndDate(), keyUsages, extendedKeyUsages);
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

        // Postavljanje vrednosti za keyUsages
        List<KeyUsageExtension> keyUsageList = new ArrayList<>();
        for (Integer keyUsageInt : keyUsages) {
            KeyUsageExtension keyUsage = KeyUsageExtension.convertIntegerToKeyUsageExtension(keyUsageInt);
            if (keyUsage != null) {
                keyUsageList.add(keyUsage);
            }
        }
        cert.setKeyUsages(keyUsageList);

        // Konvertovanje niza KeyPurposeId u listu ExtendedKey
        List<ExtendedKey> extendedKeyList = new ArrayList<>();
        for (KeyPurposeId keyPurposeId : extendedKeyUsages) {
            ExtendedKey extendedKey = extendedKeyConverter.convertToExtendedKey(keyPurposeId);
            if (extendedKey != null) {
                extendedKeyList.add(extendedKey);
            }
        }

        // Postavljanje vrednosti za extendedKeyUsages u CertificateData
        cert.setExtendedKeyUsages(extendedKeyList);



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
