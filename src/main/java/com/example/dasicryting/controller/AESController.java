package com.example.dasicryting.controller;

import com.example.dasicryting.service.AESService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")

public class AESController {

    @Autowired
    private AESService service;

    @GetMapping(value = "/getEncrypted/{file}/{secrt}")
    public void getEncrypted(@PathVariable("file") String finlename, @PathVariable("secrt") String secret) {
      //  return ResponseEntity.ok().body(service.downloadEncrypted(file, secret));
         service.downloadEncrypted(finlename,secret);

    }

    @GetMapping(value = "/getDecrypted/{file}")
    public void getDecrypted(@RequestParam("file")String file) {
        //  return ResponseEntity.ok().body(service.downloadEncrypted(file, secret));
        service.downloadDecrypted(file);

    }



}
