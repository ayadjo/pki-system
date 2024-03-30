package com.example.demo.controller;

import com.example.demo.dto.CertificateDto;
import com.example.demo.dto.RootCertificateDto;
import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;


    @PostMapping("/root-certificate/{filePass}")
    public void addRootCertificate(@RequestBody RootCertificateDto dto, @PathVariable String filePass){
       certificateService.createRootCertificate(dto, filePass);
    }

    @PostMapping("/ca-certificate/{filePass}")
    public void addCACertificate(@RequestBody CertificateDto dto, @PathVariable String filePass){
        certificateService.createCACertificate(dto, filePass);
    }

    @GetMapping("/serialNumber/{issuerMail}")
    public ResponseEntity<String> getIssuerCertificateSerialNumber(@PathVariable String issuerMail) {
        String serialNumber = certificateService.getCertificateSerialNumber(issuerMail);
        if (serialNumber != null) {
            return ResponseEntity.ok(serialNumber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/issuerCertificateType/{issuerMail}")
    public ResponseEntity<CertificateType> getIssuerCertificateType(@PathVariable String issuerMail) {
        CertificateType type = certificateService.getIssuerCertificateType(issuerMail);
        if (type != null) {
            return ResponseEntity.ok(type);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
