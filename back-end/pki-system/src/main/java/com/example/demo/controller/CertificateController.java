package com.example.demo.controller;

import com.example.demo.dto.RootCertificateDto;
import com.example.demo.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;


    @PostMapping("/root-certificate")
    public void addRootCertificate(@RequestBody RootCertificateDto dto){
        certificateService.createRootCertificate(dto);
    }
}
