package com.example.demo.model.enumerations;

public enum KeyUsageExtension {
    DIGITAL_SIGNATURE,
    NON_REPUDIATION,
    KEY_ENCIPHER,
    DATA_ENCIPHER,
    KEY_AGREEMENT,
    CERTIFICATE_SIGNING,
    CRL_SIGNING,
    ENCIPHER_ONLY,
    DECIPHER_ONLY;

    public static KeyUsageExtension convertIntegerToKeyUsageExtension(int x){
        switch (x){
            case 1<<7: return DIGITAL_SIGNATURE;
            case 1<<6: return NON_REPUDIATION;
            case 1<<5: return KEY_ENCIPHER;
            case 1<<4: return DATA_ENCIPHER;
            case 1<<3: return KEY_AGREEMENT;
            case 1<<2: return CERTIFICATE_SIGNING;
            case 1<<1: return CRL_SIGNING;
            case 1<<0: return ENCIPHER_ONLY;
            case 1<<15: return DECIPHER_ONLY;
        }
        return DIGITAL_SIGNATURE;
    }
}
