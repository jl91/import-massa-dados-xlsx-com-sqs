package com.example.elasticsearchintegration.controller;

import com.example.elasticsearchintegration.services.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("search")
public class ElasticSearch {

    @Autowired
    ElasticSearchService elasticCacheService;

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
}
