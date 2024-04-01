package com.example.demo.controller;
import com.example.demo.dto.KeyStoreDto;
import com.example.demo.dto.RootCertificateDto;
import com.example.demo.dto.ViewCerificateDto;
import com.example.demo.model.CertificateData;
import com.example.demo.model.KeyStoreAccess;
import com.example.demo.dto.CertificateDto;
import com.example.demo.dto.RootCertificateDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.CertificateData;
import com.example.demo.model.User;
import com.example.demo.model.enumerations.CertificateType;
import com.example.demo.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    @GetMapping("/all")
    public ResponseEntity<List<CertificateData>> getAllCertificates() {

        List<CertificateData> certificates = certificateService.findAll();

        if ( certificates == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/certificate/{fileName}/{alias}")
    public ResponseEntity<ViewCerificateDto> getCertificate(@PathVariable String fileName, @PathVariable String alias){
        KeyStoreDto keyStoreDto = new KeyStoreDto();
        keyStoreDto.setFileName(fileName);
        keyStoreDto.setAlias(alias);

        ViewCerificateDto certificate = certificateService.getCertificate(keyStoreDto);

        if (certificate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }


    @PostMapping("/ca-certificate/{filePass}")
    public void addCACertificate(@RequestBody CertificateDto dto, @PathVariable String filePass){
        certificateService.createCACertificate(dto, filePass);
    }

    @PostMapping("/ee-certificate/{filePass}")
    public void addEECertificate(@RequestBody CertificateDto dto, @PathVariable String filePass){
        certificateService.createEECertificate(dto, filePass);
    }


    @GetMapping("/rootAndCA/{startDate}/{endDate}")
    public ResponseEntity<List<CertificateData>> getRootAndCACertificates(
            @PathVariable("startDate") String startDateStr,
            @PathVariable("endDate") String endDateStr) throws CertificateNotYetValidException, CertificateExpiredException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            List<CertificateData> certificates = certificateService.getRootAndCACertificates(startDate, endDate);

            List<CertificateData> cert = new ArrayList<>();
            for (CertificateData u : certificates) {
                cert.add(u);
            }

            return new ResponseEntity<>(cert, HttpStatus.OK);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
    }
