package com.example.exceldatabasegenerator.controller;

import com.example.exceldatabasegenerator.services.CSVGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("generator")
public class DatabaseGenerator {

    @Autowired
    CSVGenerator CSVGenerator;

    @PostMapping()
    public String generateFile(
            @RequestParam("size") final BigInteger size
    ) {

        try {
            CSVGenerator.generate(size);
            return "ok";
        } catch (Exception exception) {
            System.out.println(exception);
            return "nok";
        }

    }
}
