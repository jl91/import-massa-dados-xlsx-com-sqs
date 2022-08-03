package com.example.exceldatabasegenerator.controller;

import com.example.exceldatabasegenerator.DTO.LineDTO;
import com.example.exceldatabasegenerator.services.CSVGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

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

    @GetMapping()
    public List<LineDTO> readDatabase(
            @RequestParam("fileName") final String fileName
    ) {
        try {
            return CSVGenerator.readFile(fileName)
                    // listando tamanho do arquivo de saída para fazer conversão para json
                    .subList(0, 10000);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
