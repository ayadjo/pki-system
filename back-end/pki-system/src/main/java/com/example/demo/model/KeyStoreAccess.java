package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class KeyStoreAccess {

    @Id
    public String filename;
    public static String password;

}
