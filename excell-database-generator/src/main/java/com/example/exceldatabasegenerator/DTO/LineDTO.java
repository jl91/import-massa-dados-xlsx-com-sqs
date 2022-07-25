package com.example.exceldatabasegenerator.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LineDTO {

    private String customerName;

    private String customerType;

    private String customerDocument;

    private Integer phoneDDD;

    private boolean phoneIsChecked;

    private Integer phoneNumber;

    private String companyDocument;

    private String companyName;

    private String contractNumber;

    private BigDecimal contractValue;

    private Integer contractInstallmentsSize;

    private LocalDate contractReferenceDate;

    private String contractWarningLevel;

    public String getCustomerName() {
        return customerName;
    }

    public LineDTO setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public String getCustomerType() {
        return customerType;
    }

    public LineDTO setCustomerType(String customerType) {
        this.customerType = customerType;
        return this;
    }

    public String getCustomerDocument() {
        return customerDocument;
    }

    public LineDTO setCustomerDocument(String customerDocument) {
        this.customerDocument = customerDocument;
        return this;
    }

    public Integer getPhoneDDD() {
        return phoneDDD;
    }

    public LineDTO setPhoneDDD(Integer phoneDDD) {
        this.phoneDDD = phoneDDD;
        return this;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public LineDTO setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public boolean getPhoneIsChecked() {
        return phoneIsChecked;
    }

    public LineDTO setPhoneIsChecked(boolean phoneIsChecked) {
        this.phoneIsChecked = phoneIsChecked;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public LineDTO setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public LineDTO setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
        return this;
    }

    public BigDecimal getContractValue() {
        return contractValue;
    }

    public LineDTO setContractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
        return this;
    }

    public Integer getContractInstallmentsSize() {
        return contractInstallmentsSize;
    }

    public LineDTO setContractInstallmentsSize(Integer contractInstallmentsSize) {
        this.contractInstallmentsSize = contractInstallmentsSize;
        return this;
    }

    public String getContractWarningLevel() {
        return contractWarningLevel;
    }

    public LineDTO setContractWarningLevel(String contractWarningLevel) {
        this.contractWarningLevel = contractWarningLevel;
        return this;
    }

    public LocalDate getContractReferenceDate() {
        return contractReferenceDate;
    }

    public LineDTO setContractReferenceDate(LocalDate contractReferenceDate) {
        this.contractReferenceDate = contractReferenceDate;
        return this;
    }

    public String getCompanyDocument() {
        return companyDocument;
    }

    public LineDTO setCompanyDocument(String companyDocument) {
        this.companyDocument = companyDocument;
        return this;
    }
}
