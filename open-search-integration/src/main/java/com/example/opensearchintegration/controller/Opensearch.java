package com.example.opensearchintegration.controller;

import com.example.opensearchintegration.integration.aws.opensearch.documents.LineDocument;
import com.example.opensearchintegration.services.OpenSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("search")
@Slf4j
public class Opensearch {

    @Autowired
    OpenSearchService openSearchService;

    @PostMapping("upload")
    public String uploadDatabase(
            @RequestPart("file") final MultipartFile file
    ) {
        try {
            openSearchService.saveFile(file);
            return "OK";
        } catch (Throwable throwable) {
            System.out.println(throwable);
            return "NOK";
        }
    }

    @GetMapping()
    public List<LineDocument> all(
            @RequestParam("page") final Optional<BigInteger> page,
            @RequestParam("size") final Optional<BigInteger> size
    ) throws IOException {
        return openSearchService.fetchAll(page, size);
    }
}
