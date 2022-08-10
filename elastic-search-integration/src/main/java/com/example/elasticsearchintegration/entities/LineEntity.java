package com.example.elasticsearchintegration.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Document(indexName = "lines")
public class LineEntity implements Serializable {

    @Id
    private UUID uuid;

    @Field(name = "contract_number")
    private String contractNumber;

    @Field(name = "customer_document")
    private String customerDocument;

    @Field(name = "customer_name")
    private String customerName;

    @Field(name = "customer_type")
    private String customerType;

    @Field(name = "phone_ddd")
    private Integer phoneDDD;

    @Field(name = "phone_is_checked")
    private boolean phoneIsChecked;

    @Field(name = "phone_number")
    private Integer phoneNumber;

    @Field(name = "company_document")
    private String companyDocument;

    @Field(name = "company_name")
    private String companyName;

    @Field(name = "contract_value")
    private BigDecimal contractValue;

    @Field(name = "contract_installments_size")
    private Integer contractInstallmentsSize;

    @Field(name = "contract_reference_date")
    private LocalDate contractReferenceDate;

    @Field(name = "contract_warning_level")
    private String contractWarningLevel;
}
