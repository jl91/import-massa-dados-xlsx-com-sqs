package com.example.api.integration.sqs.publishers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LineToSave {

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_type")
    private String customerType;

    @JsonProperty("customer_document")
    private String customerDocument;

    @JsonProperty("phone_ddd")
    private Integer phoneDDD;

    @JsonProperty("phone_is_checked")
    private boolean phoneIsChecked;

    @JsonProperty("phone_number")
    private Integer phoneNumber;

    @JsonProperty("company_document")
    private String companyDocument;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("contract_number")
    private String contractNumber;

    @JsonProperty("contract_value")
    private BigDecimal contractValue;

    @JsonProperty("contract_instalments_size")
    private Integer contractInstallmentsSize;

    @JsonProperty("contract_reference_date")
    private String contractReferenceDate;

    @JsonProperty("contract_warning_level")
    private String contractWarningLevel;
}
