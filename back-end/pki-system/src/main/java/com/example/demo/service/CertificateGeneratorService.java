package com.example.demo.service;

import com.example.demo.model.IssuerData;
import com.example.demo.model.SubjectData;
import com.example.demo.model.User;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static java.lang.Math.abs;

@Service
public class CertificateGeneratorService {
    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IssuerData generateIssuerData(PrivateKey issuerKey, User issuer) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, issuer.getCommonName());
        builder.addRDN(BCStyle.SURNAME, issuer.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, issuer.getGivenName());
        builder.addRDN(BCStyle.O, issuer.getOrganization());
        builder.addRDN(BCStyle.OU, issuer.getOrganizationUnit());
        builder.addRDN(BCStyle.C, issuer.getCountry());
        builder.addRDN(BCStyle.E, issuer.getMail());
        builder.addRDN(BCStyle.UID, issuer.getId().toString());

        return new IssuerData(issuerKey, builder.build());
    }

    SubjectData generateSubjectData(KeyPair keyPairSubject, User subject, Date startDate, Date endDate){
        String certificateSerialNumber= generateCertificateSerialNumber();

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, subject.getCommonName());
        builder.addRDN(BCStyle.SURNAME, subject.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, subject.getGivenName());
        builder.addRDN(BCStyle.O, subject.getOrganization());
        builder.addRDN(BCStyle.OU, subject.getOrganizationUnit());
        builder.addRDN(BCStyle.C, subject.getCountry());
        builder.addRDN(BCStyle.E, subject.getMail());
        builder.addRDN(BCStyle.UID, subject.getId().toString());

        SubjectData subjectData=new SubjectData();
        subjectData.setSerialNumber(certificateSerialNumber);
        subjectData.setX500name(builder.build());
        subjectData.setPublicKey(keyPairSubject.getPublic());
        subjectData.setStartDate(startDate);
        subjectData.setEndDate(endDate);
        return subjectData;

    }

    private String generateCertificateSerialNumber(){
        try {
            Random random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            Integer number=abs(random.nextInt());
            return number.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

}
