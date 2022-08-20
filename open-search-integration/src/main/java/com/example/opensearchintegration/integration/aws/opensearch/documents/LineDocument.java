package com.example.opensearchintegration.integration.aws.opensearch.documents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class LineDocument extends BaseDocument {

    @JsonProperty("contract_number")
    private String contractNumber;

    @JsonProperty("customer_document")
    private String customerDocument;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_type")
    private String customerType;

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

    @JsonProperty("contract_value")
    private BigDecimal contractValue;

    @JsonProperty("contract_installments_size")
    private Integer contractInstallmentsSize;

    @JsonProperty("contract_reference_date")
    private String contractReferenceDate;

    @JsonProperty("contract_warning_level")
    private String contractWarningLevel;
}
