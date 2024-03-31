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
    public String fileName;

    public String filePass;

    public KeyStoreAccess(String fileName, String filePass) {
        this.fileName = fileName;
        this.filePass = filePass;
    }

    public KeyStoreAccess() {


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFilePass(String filePass) {
        this.filePass = filePass;
    }

    public String getFilePass() {
        return filePass;
    }
}
