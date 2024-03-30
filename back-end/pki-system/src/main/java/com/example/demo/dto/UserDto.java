package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    Long id;
    private String mail;
    private String name;
    private String country;
    private String organizationUnit;

    public UserDto(User user) {
        id = user.getId();
        mail = user.getMail();
        name = user.getCommonName();
        country = user.getCountry();
        organizationUnit = user.getOrganizationUnit();

    }

}
