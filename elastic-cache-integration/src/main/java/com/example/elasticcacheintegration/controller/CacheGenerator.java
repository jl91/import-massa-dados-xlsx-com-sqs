package com.example.elasticcacheintegration.controller;

import com.example.elasticcacheintegration.entities.LineEntity;
import com.example.elasticcacheintegration.services.ElasticCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cache")
public class CacheGenerator {


    @Autowired
    ElasticCacheService elasticCacheService;

    @PostMapping()
    public String generateFile(
            @RequestParam("fileName") final String fileName
    ) {

        try {
            final var totalItemsLoaded = elasticCacheService.loadCSVToCache(fileName);
            return String.format("Total Items Loaded: %d", totalItemsLoaded);

        } catch (Exception exception) {
            System.out.println(exception);
            return "nok";
        }

    }

    @GetMapping()
    public List<LineEntity> readDatabase(
            @RequestParam("size") Optional<BigInteger> size,
            @RequestParam("page") Optional<BigInteger> page,
            @RequestParam("term") Optional<String> term

    ) {
        try {


            if (page.isEmpty()) {
                page = Optional.of(BigInteger.valueOf(0));
            }

            if (size.isEmpty()) {
                size = Optional.of(BigInteger.valueOf(10));
            }

            return elasticCacheService.fetchAllBy(
                            term,
                            page,
                            size
                    );

        } catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
