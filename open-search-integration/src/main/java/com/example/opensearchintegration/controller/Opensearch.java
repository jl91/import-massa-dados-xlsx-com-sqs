package com.example.opensearchintegration.controller;

import com.example.opensearchintegration.integration.aws.opensearch.entities.LineEntity;
import com.example.opensearchintegration.services.OpenSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("search")
public class Opensearch {

    @Autowired
    OpenSearchService elasticCacheService;

    @PostMapping("upload")
    public String uploadDatabase(
            @RequestPart("file") final MultipartFile file
    ) {
        try {
            elasticCacheService.saveFile(file);
            return "OK";
        } catch (Throwable throwable) {

            return "NOK";
        }
    }

//    @PostMapping("create-index/{name}")
//    public String createIndex(
//            @PathVariable("name") final String name
//    ) {
//        try {
//            elasticCacheService.createIndex(name);
//            return "OK";
//        } catch (Throwable throwable) {
//            System.out.println(throwable);
//            return "NOK";
//        }
//    }

    @GetMapping()
    public List<LineEntity> all(
            @RequestParam("page") final Optional<BigInteger> page,
            @RequestParam("size") final Optional<BigInteger> size
    ) {
        return elasticCacheService.fetchAll(page, size);
    }
}
