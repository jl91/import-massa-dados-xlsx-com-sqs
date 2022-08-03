package com.example.exceldatabasegenerator.services;

import com.example.exceldatabasegenerator.DTO.LineDTO;
import com.github.javafaker.Faker;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.RowValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class CSVGenerator {

    @Value("${spring.application.database-output}")
    private String outputPath;

    private String filePath;

    private Faker faker = new Faker(new Locale("pt-BR"));

    private Set<Integer> contractsNumbers = new HashSet<Integer>();

    private Set<String> customersDocuments = new HashSet<String>();

    private Map<String, String> companiesDocuments = new HashMap<String, String>();

    private String[] headers = {
            "CUSTOMER_NAME",
            "CUSTOMER_TYPE",
            "CUSTOMER_DOCUMENT",
            "PHONE_DDD",
            "PHONE_IS_CHECKED",
            "PHONE_NUMBER",
            "COMPANY_DOCUMENT",
            "COMPANY_NAME",
            "CONTRACT_NUMBER",
            "CONTRACT_VALUE",
            "CONTRACT_INSTALLMENTS_SIZE",
            "CONTRACT_REFERENCE_DATE",
            "CONTRACT_WARNING_LEVEL"
    };

    private String[] contractLevels = {"low", "medium", "high"};


    public void generate(
            final BigInteger quantityOfLines
    ) throws IOException {

        final var file = getFile("");

        final var writer = new CSVWriter(new FileWriter(file));

        System.out.println(
                String.format(
                        "Generating in memory database with %d lines",
                        quantityOfLines
                )
        );

        final var inMemoryBase = generateInMemoryBase(quantityOfLines);
        final var inMemoryBaseSize = inMemoryBase.size();
        final var inMemoryBaseIterator = inMemoryBase.iterator();

        System.out.println(
                String.format(
                        "In Memory Base Generated with %d lines",
                        inMemoryBaseSize
                )
        );

        writer.writeNext(headers);

        while (inMemoryBaseIterator.hasNext()) {
            final var current = inMemoryBaseIterator.next();
            writer.writeNext(current.toArray());
        }

        System.out.println(
                String.format("Writing on Excel file %s Started ...", filePath)
        );

        writer.close();

        System.out.println(
                String.format("Writing on Excel file %s Finished ...", filePath)
        );
    }

    private File getFile(
            final String fileName
    ) {
        final var name = fileName.length() > 0
                ? fileName
                : "contracts";

        filePath = getPathName(name);

        System.out.println(String.format("Creating file %s", filePath));

        return new File(filePath);
    }

    private String getPathName(final String name) {
        return String.format(
                "%s/%s-%s.csv",
                outputPath,
                name,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss"))
        );
    }

    private ArrayList<LineDTO> generateInMemoryBase(
            final BigInteger quantityOfLines
    ) {
        final ArrayList<LineDTO> lines = new ArrayList<LineDTO>();

        for (var i = 0; i < quantityOfLines.intValue(); i++) {
            var line = generateLine();
            lines.add(line);
            System.out.println(String.format("generating %d", i));
        }
        return lines;
    }

    private LineDTO generateLine() {

        final var phonenumber = faker.phoneNumber()
                .cellPhone()
                .replaceAll("\\([0-9]{0,2}\\)\\ ", "")
                .replaceAll("[^0-9]+", "");


        // Generating unique contract number
        var contractNumber = faker.random().nextInt(10000000, 99999999);
        while (contractsNumbers.contains(contractNumber)) {
            contractNumber = faker.random().nextInt(10000000, 99999999);
        }

        // Generating unique document number
        var customerDocument = faker.number().digits(11);
        while (customersDocuments.contains(customerDocument)) {
            customerDocument = faker.number().digits(11);
        }

        // Generating company document and name document number
        var companyDocument = faker.number().digits(14);
        var companyName = faker.company().name();

        if (!companiesDocuments.containsKey(companyDocument)) {
            companiesDocuments.put(companyDocument, companyName);
        }

        companyName = companiesDocuments.get(companyDocument);


        return new LineDTO()
                .setCustomerName(faker.name().name())
                .setCustomerDocument(customerDocument)
                .setCustomerType("PF")
                .setPhoneDDD(Integer.parseInt(faker.number().digits(2)))
                .setPhoneIsChecked(faker.random().nextBoolean())
                .setPhoneNumber(Integer.parseInt(phonenumber))
                .setCompanyDocument(companyDocument)
                .setCompanyName(companyName)
                .setContractNumber("" + contractNumber)
                .setContractInstallmentsSize(faker.random().nextInt(1, 99))
                .setContractReferenceDate(LocalDate.now())
                .setContractValue(BigDecimal.valueOf(faker.number().randomDouble(2, 100000000, 999999999)))
                .setContractWarningLevel(contractLevels[faker.random().nextInt(0, 2)]);
    }


    public List<LineDTO> readFile(
            final String fileName
    ) throws FileNotFoundException {


        final List<LineDTO> result = new ArrayList();
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
            final LineDTO lineDTO = new LineDTO();

            lineDTO.setCustomerName(current[0])
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

            result.add(lineDTO);

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
