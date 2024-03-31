package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class UserDataDto {
    private String userID;
    private String mail;
    private String country;
    private String organizationUnit;
    private String organization;
    private String givenName;
    private String surname;
    private String commonName;

    public UserDataDto(String data) {
        String[] params = data.split(",");
        userID = params[0].split("=")[1];
        mail = decodeHexString(params[1].split("=")[1]);
        country = params[2].split("=")[1];
        organizationUnit = params[3].split("=")[1];
        organization = params[4].split("=")[1];
        givenName = decodeHexString(params[5].split("=")[1]);
        surname = decodeHexString(params[6].split("=")[1]);
        commonName = params[7].split("=")[1];
    }

    private String decodeHexString(String hexString) {
        hexString = hexString.substring(5);

        BigInteger bigInteger = new BigInteger(hexString, 16);
        return new String(bigInteger.toByteArray(), StandardCharsets.UTF_8);
    }
}
