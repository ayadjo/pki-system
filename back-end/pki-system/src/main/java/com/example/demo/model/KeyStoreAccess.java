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
    private String fileName;

    private String filePass;

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
