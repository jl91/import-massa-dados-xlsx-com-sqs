package com.example.elasticcacheintegration.services;

import com.example.elasticcacheintegration.entities.LineEntity;
import com.example.elasticcacheintegration.repositories.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ElasticCacheService {

    @Autowired
    CSVReader CSVReader;

    @Autowired
    LineRepository lineRepository;


    public BigInteger loadCSVToCache(
            final String fileName
    ) throws FileNotFoundException {

        final var list = CSVReader.readFile(fileName);

        // só apaga a base atual se conseguir ler a base do redis, geralmente usa-se um
        lineRepository.deleteAll();

        final var totalLines = list.size();

        // Limitando pq a minha base tinha mais de 1kk de registros;
        lineRepository.saveAll(list.subList(1, 100));

// Operação com iterator

//        final var csvDatabaseIterator = list.iterator();
//        while (csvDatabaseIterator.hasNext()) {
//            final var currentEntity = csvDatabaseIterator.next();
//            lineRepository.save(currentEntity);
//        }

        return BigInteger.valueOf(totalLines);
    }

    public List<LineEntity> fetchAllBy(
            final Optional<String> term,
            final Optional<BigInteger> page,
            final Optional<BigInteger> size
    ) {
        final var pageable = PageRequest.of(
                page.get().intValue(),
                size.get().intValue()
        );

        if (term.isPresent()) {
            // Busca com CustomQuery

            return List.of(
                    lineRepository.findById(term.get()).get()
            );
        }

        return lineRepository.findAll(pageable).getContent();
    }


}
