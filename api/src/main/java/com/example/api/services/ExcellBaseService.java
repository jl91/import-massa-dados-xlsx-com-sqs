package com.example.api.services;

import com.example.api.integration.s3.AWSS3Service;
import com.example.api.integration.sqs.configurations.SQSQueues;
import com.example.api.integration.sqs.publishers.messages.LineToSave;
import com.example.api.integration.sqs.publishers.messages.NewFileUploaded;
import com.github.pjfanning.xlsx.StreamingReader;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcellBaseService {

    @Autowired
    AWSS3Service awss3Service;

    @Value("${spring.application.s3.bucket.base-excell}")
    public String baseExcellBucketName;

    @Autowired
    private QueueMessagingTemplate messagingTemplate;

    @Autowired
    private SQSQueues sqsQueues;

    @Value("${spring.application.base-excell-destination}")
    private String destinationPath;

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

    private HashMap<String, Integer> headerCoords = new HashMap<String, Integer>();

    private HashSet<LineToSave> linesToSave = new HashSet<>();


    public boolean saveUploadedFile(final MultipartFile file) throws IOException {
        awss3Service.saveMultipartFileOnS3(
                file,
                baseExcellBucketName
        );

        dispatchNewFileUploaded(
                baseExcellBucketName,
                file.getOriginalFilename()
        );

        return true;
    }


    private void dispatchNewFileUploaded(
            final String bucket,
            final String filename
    ) {
        final var messageObject = new NewFileUploaded();
        messageObject.setBucket(bucket);
        messageObject.setFileName(filename);

        messagingTemplate.convertAndSend(
                sqsQueues.getNewFileUploaded(),
                messageObject
        );
    }

    public void processBaseExcellFromS3(
            final String bucket,
            final String fileName
    ) throws IOException {

        final var file = getFileFromS3(bucket, fileName);
        final var workbook = StreamingReader
                .builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(file);

        final var sheet = workbook.getSheetAt(0);

        final var rowIterator = sheet.rowIterator();

        while (rowIterator.hasNext()) {

            final var row = rowIterator.next();
            final var currentRowNumber = row.getRowNum();

            if (currentRowNumber == 0) {
                // Do Another header validations;
                mapHeaders(row);
                continue;
            }

            processRow(row);
        }

        dispatchLines();

        // Apaga o arquivo depois de processar todas as linhas
        if (file.exists()) {
            file.delete();
        }

    }

    private void mapHeaders(Row row) {

        final var cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            final var cell = cellIterator.next();

            final var cellValue = cell.getStringCellValue();

            if (!Arrays.stream(headers).anyMatch(header -> header.equals(cellValue))) {
                throw new RuntimeException(
                        String.format("Invalid header %s on excell provided", cellValue)
                );
            }

            for (int i = 0; i < headers.length; i++) {

                final var currentHeader = headers[i];

                if (currentHeader.equals(cellValue)) {
                    this.headerCoords.put(currentHeader, i);
                }
            }

        }

    }

    private void processRow(Row row) {

        final var customerName = row.getCell(this.headerCoords.get("CUSTOMER_NAME"));
        final var customerType = row.getCell(this.headerCoords.get("CUSTOMER_TYPE"));
        final var customerDocument = row.getCell(this.headerCoords.get("CUSTOMER_DOCUMENT"));
        final var phoneDDD = row.getCell(this.headerCoords.get("PHONE_DDD"));
        final var phoneIsChecked = row.getCell(this.headerCoords.get("PHONE_IS_CHECKED"));
        final var phoneNumber = row.getCell(this.headerCoords.get("PHONE_NUMBER"));
        final var companyDocument = row.getCell(this.headerCoords.get("COMPANY_DOCUMENT"));
        final var companyName = row.getCell(this.headerCoords.get("COMPANY_NAME"));
        final var contractNumber = row.getCell(this.headerCoords.get("CONTRACT_NUMBER"));
        final var contractValue = row.getCell(this.headerCoords.get("CONTRACT_VALUE"));
        final var contractInstallmentsSize = row.getCell(this.headerCoords.get("CONTRACT_INSTALLMENTS_SIZE"));
        final var contractReferenceDate = row.getCell(this.headerCoords.get("CONTRACT_REFERENCE_DATE"));
        final var contractWarningLevel = row.getCell(this.headerCoords.get("CONTRACT_WARNING_LEVEL"));

        final var line = new LineToSave();

        line.setCustomerName(customerName.getStringCellValue());
        line.setCustomerType(customerType.getStringCellValue());
        line.setCustomerDocument(customerDocument.getStringCellValue());

        line.setPhoneDDD(Integer.parseInt(phoneDDD.getStringCellValue()));
        line.setPhoneIsChecked(phoneIsChecked.getBooleanCellValue());
        line.setPhoneNumber(Integer.parseInt(phoneNumber.getStringCellValue()));

        line.setCompanyDocument(companyDocument.getStringCellValue());
        line.setCompanyName(companyName.getStringCellValue());

        line.setContractNumber(contractNumber.getStringCellValue());
        line.setContractValue(BigDecimal.valueOf(contractValue.getNumericCellValue()));
        line.setContractInstallmentsSize(Integer.parseInt(contractInstallmentsSize.getStringCellValue()));
        line.setContractReferenceDate(contractReferenceDate.getStringCellValue());
        line.setContractWarningLevel(contractWarningLevel.getStringCellValue());

        batchLine(line);

    }

    private void batchLine(
            final LineToSave line
    ) {

        if (linesToSave.size() < 20) {
            linesToSave.add(line);
            return;
        }

        dispatchLines();
    }

    private void dispatchLines() {

        if (linesToSave.size() <= 0) {
            return;
        }

        final var list = linesToSave.stream().collect(Collectors.toList());

        messagingTemplate.convertAndSend(
                sqsQueues.getLinesToSave(),
                list
        );

        linesToSave.clear();

    }

    private File getFileFromS3(
            final String bucket,
            final String fileName
    ) throws IOException {
        final var s3Object = awss3Service.getFileFromS3(bucket, fileName);

        final var s3ObjectContent = s3Object.getObjectContent();

        final var contentBytes = IOUtils.toByteArray(s3ObjectContent);

        final var file = new File(
                destinationPath + "/" + fileName
        );

        final var outputStream = new FileOutputStream(file, true);

        outputStream.write(contentBytes);
        outputStream.close();

        return file;
    }

    public void saveLines(
            final List<LineToSave> lines
    ) {
//        lines.stream().forEach(line -> );

        // Do Mysql Operations
        System.out.println(lines);
    }

}
