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
            case 0: return DIGITAL_SIGNATURE;
            case 1: return NON_REPUDIATION;
            case 2: return KEY_ENCIPHER;
            case 3: return DATA_ENCIPHER;
            case 4: return KEY_AGREEMENT;
            case 5: return CERTIFICATE_SIGNING;
            case 6: return CRL_SIGNING;
            case 7: return ENCIPHER_ONLY;
            case 8: return DECIPHER_ONLY;
        }
        return DIGITAL_SIGNATURE;
    }
}
