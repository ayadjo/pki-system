package com.example.demo.converter;

import com.example.demo.model.enumerations.ExtendedKey;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExtendedKeyConverter {
    public KeyPurposeId[] convertToExtendedKey(List<ExtendedKey> extendedKeyUsages){
        if (extendedKeyUsages.isEmpty()){
            return new KeyPurposeId[0];
        }
        KeyPurposeId[] keyPurposeIds=new KeyPurposeId[extendedKeyUsages.size()];
        int i=0;
        for(ExtendedKey extendedKeyUsage:extendedKeyUsages){
            keyPurposeIds[i++]=convertToExtendedKey(extendedKeyUsage);
        }
        return keyPurposeIds;
    }

    private KeyPurposeId convertToExtendedKey(ExtendedKey keyUsage){
        switch (keyUsage){
            case IPSEC_USER:return KeyPurposeId.id_kp_ipsecUser;
            case EMAIL_PROTECTION:return KeyPurposeId.id_kp_emailProtection;
            case TSL_WEB_SERVER_AUTHENTICATION:return KeyPurposeId.id_kp_serverAuth;
            case TLS_WEB_CLIENT_AUTHENTICATION:return KeyPurposeId.id_kp_clientAuth;
            case TIMESTAMPING:return KeyPurposeId.id_kp_timeStamping;
            case SIGN_EXECUTABLE_CODE:return KeyPurposeId.id_kp_codeSigning;
            case IPSEC_TUNNEL:return KeyPurposeId.id_kp_ipsecTunnel;
            case IPSEC_END_SYSTEM:return KeyPurposeId.id_kp_ipsecEndSystem;
        }
        return KeyPurposeId.anyExtendedKeyUsage;
    }

    public ArrayList<ExtendedKey> getExtendedKeyUsage(List<String> extendedKeyUsage) {
        ArrayList<ExtendedKey> extendedKeyUsages = new ArrayList<ExtendedKey>();

        // https://docs.aws.amazon.com/acm/latest/APIReference/API_ExtendedKeyUsage.html

        for(String extKU : extendedKeyUsage){
            int num = Integer.parseInt(extKU.split("\\.")[8]);

            switch (num){
                // 1.3.6.1.5.5.7.3.1 (TLS_WEB_SERVER_AUTHENTICATION)
                case 1: extendedKeyUsages.add(ExtendedKey.TSL_WEB_SERVER_AUTHENTICATION); break;

                // 1.3.6.1.5.5.7.3.2 (TLS_WEB_CLIENT_AUTHENTICATION)
                case 2: extendedKeyUsages.add(ExtendedKey.TLS_WEB_CLIENT_AUTHENTICATION); break;

                // 1.3.6.1.5.5.7.3.3 (CODE_SIGNING)
                case 3: extendedKeyUsages.add(ExtendedKey.SIGN_EXECUTABLE_CODE); break;

                // 1.3.6.1.5.5.7.3.4 (EMAIL_PROTECTION)
                case 4: extendedKeyUsages.add(ExtendedKey.EMAIL_PROTECTION); break;

                // 1.3.6.1.5.5.7.3.5 (IPSEC_END_SYSTEM)
                case 5: extendedKeyUsages.add(ExtendedKey.IPSEC_END_SYSTEM); break;

                // 1.3.6.1.5.5.7.3.6 (IPSEC_TUNNEL)
                case 6: extendedKeyUsages.add(ExtendedKey.IPSEC_TUNNEL); break;

                // 1.3.6.1.5.5.7.3.7 (IPSEC_USER)
                case 7: extendedKeyUsages.add(ExtendedKey.IPSEC_USER); break;

                // 1.3.6.1.5.5.7.3.8 (TIME_STAMPING)
                case 8: extendedKeyUsages.add(ExtendedKey.TIMESTAMPING); break;

                // 1.3.6.1.5.5.7.3.9 (OCSP_SIGNING)
                case 9: extendedKeyUsages.add(ExtendedKey.SIGN_EXECUTABLE_CODE); break;
            }
        }

        return extendedKeyUsages;
    }

    public ExtendedKey convertToExtendedKey(KeyPurposeId keyPurposeId) {
        if (keyPurposeId == null) {
            return null;
        }

        if (KeyPurposeId.id_kp_serverAuth.equals(keyPurposeId)) {
            return ExtendedKey.TSL_WEB_SERVER_AUTHENTICATION;
        } else if (KeyPurposeId.id_kp_clientAuth.equals(keyPurposeId)) {
            return ExtendedKey.TLS_WEB_CLIENT_AUTHENTICATION;
        } else if (KeyPurposeId.id_kp_codeSigning.equals(keyPurposeId)) {
            return ExtendedKey.SIGN_EXECUTABLE_CODE;
        } else if (KeyPurposeId.id_kp_emailProtection.equals(keyPurposeId)) {
            return ExtendedKey.EMAIL_PROTECTION;
        } else if (KeyPurposeId.id_kp_ipsecEndSystem.equals(keyPurposeId)) {
            return ExtendedKey.IPSEC_END_SYSTEM;
        } else if (KeyPurposeId.id_kp_ipsecTunnel.equals(keyPurposeId)) {
            return ExtendedKey.IPSEC_TUNNEL;
        } else if (KeyPurposeId.id_kp_ipsecUser.equals(keyPurposeId)) {
            return ExtendedKey.IPSEC_USER;
        } else if (KeyPurposeId.id_kp_timeStamping.equals(keyPurposeId)) {
            return ExtendedKey.TIMESTAMPING;
        } else {
            // In case of any other key purpose id, return null or handle as needed
            return null;
        }
    }

}
