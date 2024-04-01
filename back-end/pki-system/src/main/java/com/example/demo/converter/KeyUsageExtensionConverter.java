package com.example.demo.converter;

import com.example.demo.model.enumerations.KeyUsageExtension;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyUsageExtensionConverter {
    public  Integer[] convertKeyUsageToInteger(List<KeyUsageExtension> keyUsageList){
        if (keyUsageList.isEmpty()){
            return new Integer[0];
        }
        Integer [] keyUsageArray=new Integer[keyUsageList.size()];
        int i=0;
        for(KeyUsageExtension keyUsage:keyUsageList){
            keyUsageArray[i++]=getUsageNumber(keyUsage);
        }
        return keyUsageArray;
    }

    private Integer getUsageNumber(KeyUsageExtension keyUsage){
        switch (keyUsage){
            case DIGITAL_SIGNATURE:return 1<<7;
            case NON_REPUDIATION:return 1<<6;
            case KEY_ENCIPHER:return 1<<5;
            case DATA_ENCIPHER:return 1<<4;
            case KEY_AGREEMENT:return 1<<3;
            case CERTIFICATE_SIGNING:return 1<<2;
            case CRL_SIGNING:return 1<<1;
            case ENCIPHER_ONLY:return 1<<0;
            case DECIPHER_ONLY:return 1<<15;
        }

        return null;
    }

    public List<KeyUsageExtension> getKeyUsageFromBooleanArr(boolean[] keyUsageBool){
        List<KeyUsageExtension> keyUsages = new ArrayList<KeyUsageExtension>();
        for(int i = 0; i <= 8; i++){
            if(keyUsageBool[i]){
                keyUsages.add(KeyUsageExtension.convertIntegerToKeyUsageExtension(i));
            }
        }
        return keyUsages;
    }
}
