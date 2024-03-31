package com.example.demo.keystores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeyStoreWriter {
    //KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    //Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;

    public KeyStoreWriter() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);//svaki keyStore ima svoju lozinku
            } else {
                //Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //ovo je poslednji korak kako bi ostalo u file sistemu
    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //pod aliasom cuvamo sertifikate
    public void write(String alias, PrivateKey privateKey, char[] password, Certificate[] certificateChain) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, certificateChain);
            X509Certificate certificate=(X509Certificate)keyStore.getCertificate("alias");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void writeEE(String alias, PrivateKey privateKey, char[] password, Certificate[] certificateChain) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, certificateChain);
            X509Certificate certificate=(X509Certificate)keyStore.getCertificate("alias");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /*public void setCertificateChain(String alias, Certificate[] certificateChain, Certificate subjectCertificate) {
        try {
            List<Certificate> newChainList = new ArrayList<>(Arrays.asList(certificateChain));
            newChainList.add(0, subjectCertificate);
            Collections.reverse(newChainList);

            for (int i = 0; i < newChainList.size() - 1; i++) {
                keyStore.setCertificateEntry(alias, newChainList.get(i));
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }*/




}
