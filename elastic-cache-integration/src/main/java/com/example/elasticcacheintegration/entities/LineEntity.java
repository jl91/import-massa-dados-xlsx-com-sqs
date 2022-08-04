package com.example.elasticcacheintegration.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@RedisHash("Line")
public class LineEntity implements Serializable {

    @Id
    private String contractNumber;

    private String customerDocument;

    private String customerName;

    private String customerType;

    private Integer phoneDDD;

    private boolean phoneIsChecked;

    private Integer phoneNumber;

    private String companyDocument;

    private String companyName;

    private BigDecimal contractValue;

    private Integer contractInstallmentsSize;

    private LocalDate contractReferenceDate;

    private String contractWarningLevel;
}
