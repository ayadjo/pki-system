package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
@Getter
@Setter
public class KeyStoreAccess {

    @Id
    String fileName;

    String filePass;
}
