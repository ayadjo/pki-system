package com.example.demo.service;

import com.example.demo.dto.CertificateDto;
import com.example.demo.dto.RootCertificateDto;
import com.example.demo.keystores.KeyStoreReader;
import com.example.demo.keystores.KeyStoreWriter;
import com.example.demo.model.*;
import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.KeyStoreAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
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
    public void createRootCertificate(RootCertificateDto root, String pass) {
        if (root == null || pass == null) {
            return;
        }

        User issuer = userService.getByUsername(root.getIssuerMail());
        if (issuer == null) {
            throw new IllegalArgumentException("Issuer cannot be null");
        }

        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        //proveri da li treba key pair ili public key
        SubjectData subjectData = certificateGeneratorService.generateSubjectData(keyPair, issuer, root.getStartDate(), root.getEndDate());
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(keyPair.getPrivate(), issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        createCertificateEntry(certificate, CertificateType.ROOT, issuer, issuer, root.getStartDate(), root.getEndDate());

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

    public void createCACertificate(CertificateDto cert, String pass){
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

        SubjectData subjectData = certificateGeneratorService.generateSubjectData(subjectKeyPair, subject, cert.getStartDate(), cert.getEndDate());
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(issuerPrivateKey, issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        createCertificateEntry(certificate, CertificateType.CA, issuer, subject, cert.getStartDate(), cert.getEndDate());

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

        SubjectData subjectData = certificateGeneratorService.generateSubjectData(subjectKeyPair, subject, cert.getStartDate(), cert.getEndDate());
        IssuerData issuerData = certificateGeneratorService.generateIssuerData(issuerPrivateKey, issuer);
        X509Certificate certificate = certificateGeneratorService.generateCertificate(subjectData, issuerData);

        createCertificateEntry(certificate, CertificateType.EE, issuer, subject, cert.getStartDate(), cert.getEndDate());

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
    private void createCertificateEntry(X509Certificate certificate, CertificateType certificateType, User issuer, User subject, Date startDate, Date endDate) {
        CertificateData newCert = new CertificateData();
        newCert.setSerialNumber(certificate.getSerialNumber().toString());
        newCert.setRevoked(false);
        newCert.setCertificateType(certificateType);
        newCert.setIssuerMail(issuer.getMail());
        newCert.setSubjectMail(subject.getMail());
        newCert.setStartDate(startDate);
        newCert.setEndDate(endDate);
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




}
