package com.example.elasticcacheintegration.services;

import com.example.elasticcacheintegration.entities.LineEntity;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.RowValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class CSVReader {

    @Value("${spring.application.database-output}")
    private String outputPath;

    public List<LineEntity> readFile(
            final String fileName
    ) throws FileNotFoundException {

        final List<LineEntity> result = new ArrayList();
        final var url = outputPath + File.separator + fileName;
        final var reader = new CSVReaderBuilder(new FileReader(url))
                .withRowValidator(fabricateCustomValidator())
                .build();

        final var readerIterator = reader.iterator();

        //pulando a primeira linha ( header )
        if(readerIterator.hasNext()){
            readerIterator.next();
        }

        while (readerIterator.hasNext()){
            final var current = readerIterator.next();
            final LineEntity lineEntity = new LineEntity();

            lineEntity.setCustomerName(current[0])
                    .setCustomerType(current[1])
                    .setCustomerDocument(current[2])
                    .setPhoneDDD(Integer.parseInt(current[3]))
                    .setPhoneIsChecked(current[4].equals("1"))
                    .setPhoneNumber(Integer.parseInt(current[5]))
                    .setCompanyDocument(current[6])
                    .setCompanyName(current[7])
                    .setContractNumber(current[8])
                    .setContractValue(new BigDecimal(current[9]))
                    .setContractInstallmentsSize(Integer.parseInt(current[10]))
                    .setContractReferenceDate(LocalDate.parse(current[11]))
                    .setContractWarningLevel(current[12])
            ;

            result.add(lineEntity);

        }

        return result;
    }


    private RowValidator fabricateCustomValidator() {
        return new RowValidator() {
            @Override
            public boolean isValid(String[] strings) {
                System.out.println(strings);
                return true;
            }

            @Override
            public void validate(String[] strings) throws CsvValidationException {

            }
        };
    }


}
