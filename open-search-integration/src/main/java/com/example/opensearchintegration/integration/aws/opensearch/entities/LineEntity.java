package com.example.opensearchintegration.integration.aws.opensearch.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Document(indexName = "lines")
public class LineEntity implements Serializable {

    @Id
    private String uuid;

    @Field(name = "contract_number", type = FieldType.Text)
    private String contractNumber;

    @Field(name = "customer_document", type = FieldType.Text)
    private String customerDocument;

    @Field(name = "customer_name", type = FieldType.Text)
    private String customerName;

    @Field(name = "customer_type", type = FieldType.Text)
    private String customerType;

    @Field(name = "phone_ddd", type = FieldType.Integer)
    private Integer phoneDDD;

    @Field(name = "phone_is_checked", type = FieldType.Boolean)
    private boolean phoneIsChecked;

    @Field(name = "phone_number", type = FieldType.Integer)
    private Integer phoneNumber;

    @Field(name = "company_document", type = FieldType.Text)
    private String companyDocument;

    @Field(name = "company_name" , type = FieldType.Text)
    private String companyName;

    @Field(name = "contract_value", type = FieldType.Double)
    private BigDecimal contractValue;

    @Field(name = "contract_installments_size", type = FieldType.Integer)
    private Integer contractInstallmentsSize;

    @Field(name = "contract_reference_date", type = FieldType.Text)
    private String contractReferenceDate;

    @Field(name = "contract_warning_level", type = FieldType.Text)
    private String contractWarningLevel;
}
