package com.example.exceldatabasegenerator.services;

import com.example.exceldatabasegenerator.DTO.LineDTO;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class ExcellGenerator {

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
    ) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final var file = getFile("");

        final var workbook = new SXSSFWorkbook(100);

        final var sheet = workbook.createSheet();

        final var inMemoryBase = generateInMemoryBase(quantityOfLines);
        final var inMemoryBaseSize = inMemoryBase.size();

        System.out.println(
                String.format(
                        "In Memory Base Generated with %d lines",
                        inMemoryBaseSize
                )
        );

        final var fields = LineDTO.class.getDeclaredFields();

        for (var rowIndex = 0; rowIndex < inMemoryBaseSize; rowIndex++) {

            final var row = sheet.createRow(rowIndex);

            final var lineDTO = inMemoryBase.get(rowIndex);

            if (rowIndex == 0) {
                for (var headerCellIndex = 0; headerCellIndex < headers.length; headerCellIndex++) {
                    final var headerCell = row.createCell(headerCellIndex, CellType.STRING);
                    headerCell.setCellValue(headers[headerCellIndex]);
                }
                continue;
            }

            for (var fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {

                final var currentField = fields[fieldIndex];

                final var cell = fabricateCell(row, currentField, fieldIndex);

                setCellValue(cell, lineDTO, currentField);
            }

        }


        System.out.println(
                String.format("Writing on Excel file %s Started ...", filePath)
        );

        final var fileOutPutStream = new FileOutputStream(file);
        workbook.write(fileOutPutStream);

        System.out.println(
                String.format("Writing on Excel file %s Finished ...", filePath)
        );
    }

    private String getMethodNameByField(
            final Field field
    ) {
        final var name = field.getName();
        final var firstPart = name.charAt(0);
        final var lastPart = name.substring(1);
        return String.format(
                "get%s%s",
                String.valueOf(firstPart).toUpperCase(),
                lastPart
        );
    }

    private void setCellValue(
            final SXSSFCell cell,
            final LineDTO lineDTO,
            final Field field
    ) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        final var methodName = getMethodNameByField(field);
        final var dynamicallyCalledMethodValue = lineDTO.getClass().getMethod(methodName).invoke(lineDTO);

        if (field.getType().equals(String.class)) {
            cell.setCellValue((String) dynamicallyCalledMethodValue);
            return;
        }

        if (field.getType().equals(BigDecimal.class)) {
            final var value = (BigDecimal) dynamicallyCalledMethodValue;
            cell.setCellValue(value.doubleValue());
            return;
        }

        if (field.getType().equals(Integer.class)) {
            cell.setCellValue((Integer) dynamicallyCalledMethodValue);
            return;
        }

        if (field.getType().equals(boolean.class)) {
            cell.setCellValue((boolean) dynamicallyCalledMethodValue);
            return;
        }

        if (field.getType().equals(LocalDate.class)) {
            final var value = (LocalDate) dynamicallyCalledMethodValue;
            cell.setCellValue(
                    value.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
            );
            return;
        }

    }

    private SXSSFCell fabricateCell(SXSSFRow row, Field field, Integer index) {

        if (
                field.getType().equals(String.class) ||
                        field.getType().equals(LocalDate.now())
        ) {
            return row.createCell(index, CellType.STRING);
        }

        if (
                field.getType().equals(Integer.class)
                        || field.getType().equals(BigDecimal.class)
        ) {
            return row.createCell(index, CellType.NUMERIC);
        }

        if (field.getType().equals(boolean.class)) {
            return row.createCell(index, CellType.BOOLEAN);
        }

        return row.createCell(index);
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
                "%s/%s-%s.xlsx",
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
                .setContractValue(BigDecimal.valueOf(faker.number().randomDouble(2, 1000000, 9999999)))
                .setContractWarningLevel(contractLevels[faker.random().nextInt(0, 2)]);
    }

}
