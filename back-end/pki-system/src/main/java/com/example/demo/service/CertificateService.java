package com.example.demo.service;

import com.example.demo.converter.ExtendedKeyConverter;
import com.example.demo.converter.KeyUsageExtensionConverter;
import com.example.demo.dto.CertificateDto;
import com.example.demo.dto.RootCertificateDto;
import com.example.demo.keystores.KeyStoreReader;
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


import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;


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
            return;
        }

        User issuer = userService.getByUsername(root.getIssuerMail());
        if (issuer == null) {
            throw new IllegalArgumentException("Issuer cannot be null");
        }

        if (root.getStartDate().after(root.getEndDate())) {
            throw new IllegalArgumentException("Invalid start or end date for certificate");
        }

        Integer[] keyUsages = keyUsageExtensionConverter.convertKeyUsageToInteger(root.getKeyUsageExtension());
        KeyPurposeId[] extendedKeyUsages = extendedKeyConverter.convertToExtendedKey(root.getExtendedKey());
        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        SubjectData subjectData = certificateGeneratorService.generateSubjectData(keyPair, issuer, root.getStartDate(), root.getEndDate(), keyUsages, extendedKeyUsages);
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(keyPair.getPrivate(), issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        createCertificateEntry(certificate, CertificateType.ROOT, issuer, issuer, root.getStartDate(), root.getEndDate(), keyUsages, extendedKeyUsages);

        String fileName = certificate.getSerialNumber().toString() + ".jks";
        String filePass = hashPassword(pass);

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null, filePass.toCharArray());
        keyStoreWriter.write(certificate.getSerialNumber().toString() + issuer.getMail(), keyPair.getPrivate(), filePass.toCharArray(), new Certificate[]{certificate});
        keyStoreWriter.saveKeyStore(fileName, filePass.toCharArray());

        KeyStoreAccess keyStoreAccess = new KeyStoreAccess();
        keyStoreAccess.setFileName(fileName);
        keyStoreAccess.setFilePass(filePass);
        keyStoreAccessRepository.save(keyStoreAccess);

    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public void createCACertificate(CertificateDto cert, String pass) {
        KeyStoreReader keyStoreReader = new KeyStoreReader();

        KeyStoreAccess keyStoreAccess = keyStoreAccessRepository.findByFileName(cert.getIssuerCertificateSerialNumber() + ".jks");
        if (keyStoreAccess == null) {
            throw new IllegalArgumentException("KeyStoreAccess not found for file name: " + cert.getIssuerCertificateSerialNumber() + ".jks");
        }
        User issuer = userService.getByUsername(cert.getIssuerMail());
        if (issuer == null) {
            throw new IllegalArgumentException("Issuer cannot be null");
        }
        User subject = userService.getByUsername(cert.getSubjectMail());
        KeyPair subjectKeyPair = certificateGeneratorService.generateKeyPair();
        PrivateKey issuerPrivateKey = keyStoreReader.readPrivateKey(keyStoreAccess.getFileName(), keyStoreAccess.getFilePass(), cert.getIssuerCertificateSerialNumber() + cert.getIssuerMail(), keyStoreAccess.getFilePass());

        Integer[] caKeyUsages = keyUsageExtensionConverter.convertKeyUsageToInteger(cert.getKeyUsageExtension());
        KeyPurposeId[] caExtendedKeyUsages = extendedKeyConverter.convertToExtendedKey(cert.getExtendedKey());

        SubjectData subjectData = certificateGeneratorService.generateSubjectData(subjectKeyPair, subject, cert.getStartDate(), cert.getEndDate(), caKeyUsages, caExtendedKeyUsages);
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(issuerPrivateKey, issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        createCertificateEntry(certificate, CertificateType.CA, issuer, subject, cert.getStartDate(), cert.getEndDate(), caKeyUsages, caExtendedKeyUsages);

        Certificate[] certificateChain=createChain(cert.getIssuerCertificateSerialNumber(),cert.getIssuerCertificateSerialNumber()+cert.getIssuerMail(),certificate);

        String fileName = certificate.getSerialNumber().toString() + ".jks";
        String filePass = hashPassword(pass);

        KeyStoreWriter keyStoreWriter=new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null, filePass.toCharArray());
        keyStoreWriter.write(certificate.getSerialNumber().toString() + subject.getMail(), subjectKeyPair.getPrivate(), filePass.toCharArray(), certificateChain);
        keyStoreWriter.saveKeyStore(fileName, filePass.toCharArray());

        KeyStoreAccess keyStoreAccess1 = new KeyStoreAccess();
        keyStoreAccess1.setFileName(fileName);
        keyStoreAccess1.setFilePass(filePass);
        keyStoreAccessRepository.save(keyStoreAccess1);
    }
    private Certificate[] createChain(String issuerCertificateSerialNumber, String issuerAlias, Certificate subjectCertificate){
        KeyStoreReader keyStoreReader=new KeyStoreReader();
        String fileName = issuerCertificateSerialNumber + ".jks";
        KeyStoreAccess keyStoreAccess = keyStoreAccessRepository.findByFileName(fileName);
        if (keyStoreAccess == null) {
            throw new IllegalArgumentException("KeyStoreAccess not found for file name: " + fileName);
        }

        String filePass = keyStoreAccess.getFilePass();
        KeyStore keyStore = keyStoreReader.getKeyStore(fileName, filePass);
        try {
            //dohvati lanac sertifikata na osnovu aliasa izdavačkog sertifikata
            Certificate[] chain = keyStore.getCertificateChain(issuerAlias);
            List<Certificate> newChainList = new ArrayList<>(Arrays.asList(chain));
            //sertifikat subjekta se dodaje na početak lanca sertifikata
            newChainList.add(0, subjectCertificate);
            return newChainList.toArray(new Certificate[0]);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void createEECertificate(CertificateDto cert, String pass){
        KeyStoreReader keyStoreReader = new KeyStoreReader();

        KeyStoreAccess keyStoreAccess = keyStoreAccessRepository.findByFileName(cert.getIssuerCertificateSerialNumber() + ".jks");
        if (keyStoreAccess == null) {
            throw new IllegalArgumentException("KeyStoreAccess not found for file name: " + cert.getIssuerCertificateSerialNumber() + ".jks");
        }
        User issuer = userService.getByUsername(cert.getIssuerMail());
        if (issuer == null) {
            throw new IllegalArgumentException("Issuer cannot be null");
        }
        User subject = userService.getByUsername(cert.getSubjectMail());
        KeyPair subjectKeyPair = certificateGeneratorService.generateKeyPair();
        PrivateKey issuerPrivateKey = keyStoreReader.readPrivateKey(keyStoreAccess.getFileName(), keyStoreAccess.getFilePass(), cert.getIssuerCertificateSerialNumber() + cert.getIssuerMail(), keyStoreAccess.getFilePass());

        Integer[] eeKeyUsages = keyUsageExtensionConverter.convertKeyUsageToInteger(cert.getKeyUsageExtension());
        KeyPurposeId[] eeExtendedKeyUsages = extendedKeyConverter.convertToExtendedKey(cert.getExtendedKey());

        SubjectData subjectData = certificateGeneratorService.generateSubjectData(subjectKeyPair, subject, cert.getStartDate(), cert.getEndDate(), eeKeyUsages, eeExtendedKeyUsages);
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(issuerPrivateKey, issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        createCertificateEntry(certificate, CertificateType.EE, issuer, subject, cert.getStartDate(), cert.getEndDate(), eeKeyUsages, eeExtendedKeyUsages);

        Certificate[] certificateChain=createChain(cert.getIssuerCertificateSerialNumber(),cert.getIssuerCertificateSerialNumber()+cert.getIssuerMail(),certificate);

        String fileName = certificate.getSerialNumber().toString() + ".jks";
        String filePass = hashPassword(pass);

        KeyStoreWriter keyStoreWriter=new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null, filePass.toCharArray());
        assert certificateChain != null;
        //za end*entity ne cuvamo private key, pravila novi nacin cuvanja
        keyStoreWriter.setCertificateChain(certificate.getSerialNumber().toString() + subject.getMail(), certificateChain, certificate);
        keyStoreWriter.saveKeyStore(fileName, filePass.toCharArray());

        KeyStoreAccess keyStoreAccess1 = new KeyStoreAccess();
        keyStoreAccess1.setFileName(fileName);
        keyStoreAccess1.setFilePass(filePass);
        keyStoreAccessRepository.save(keyStoreAccess1);
    }
    private void createCertificateEntry(X509Certificate certificate, CertificateType certificateType, User issuer, User subject, Date startDate, Date endDate, Integer[] keyUsages, KeyPurposeId[] extendedKeyUsages) {
        CertificateData newCert = new CertificateData();
        newCert.setSerialNumber(certificate.getSerialNumber().toString());
        newCert.setRevoked(false);
        newCert.setCertificateType(certificateType);
        newCert.setIssuerMail(issuer.getMail());
        newCert.setSubjectMail(subject.getMail());
        newCert.setStartDate(startDate);
        newCert.setEndDate(endDate);

        // Postavljanje vrednosti za keyUsages
        List<KeyUsageExtension> keyUsageList = new ArrayList<>();
        for (Integer keyUsageInt : keyUsages) {
            KeyUsageExtension keyUsage = KeyUsageExtension.convertIntegerToKeyUsageExtension(keyUsageInt);
            if (keyUsage != null) {
                keyUsageList.add(keyUsage);
            }
        }
        newCert.setKeyUsages(keyUsageList);

        // Konvertovanje niza KeyPurposeId u listu ExtendedKey
        List<ExtendedKey> extendedKeyList = new ArrayList<>();
        for (KeyPurposeId keyPurposeId : extendedKeyUsages) {
            ExtendedKey extendedKey = extendedKeyConverter.convertToExtendedKey(keyPurposeId);
            if (extendedKey != null) {
                extendedKeyList.add(extendedKey);
            }
        }

        // Postavljanje vrednosti za extendedKeyUsages u CertificateData
        newCert.setExtendedKeyUsages(extendedKeyList);
        certificateRepository.save(newCert);
    }



    public List<CertificateData> getRootAndCACertificates(){
        List<CertificateData> certificates = new ArrayList<>();
        for(CertificateData c : certificateRepository.findAll()){
            if(c.getCertificateType() != CertificateType.EE){
                certificates.add(c);
            }
        }
        return certificates;
    }

    public List<CertificateData> findAll() {
        return certificateRepository.findAll();
    }


    private boolean isValid(X509Certificate x509Certificate, PublicKey issuerPublicKey) {
        try {
            x509Certificate.verify(issuerPublicKey); //Verifies that this certificate was signed using the private key that corresponds to the specified public key
            x509Certificate.checkValidity();   //proverava da li je sertifikat trenutno važeći u odnosu na trenutni datum i vreme
        } catch (Exception e) {
            return false; // Sertifikat nije validan
        }

        return true;
    }
    

    private static boolean isRootCertificate(CertificateData certificate) {
        return certificate.getCertificateType() == CertificateType.ROOT;
    }



}
