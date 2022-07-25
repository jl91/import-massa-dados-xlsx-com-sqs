package com.example.api.integration.database.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContractId implements Serializable {

    private static final long serialVersionUID = -1616624974672002878L;

    @Column(name = "customer_document_number", nullable = false, length = 14)
    private String customerDocumentNumber;

    @Column(name = "company_document_number", nullable = false, length = 14)
    private String companyEntityDocumentNumber;

    @Column(name = "contract_number", nullable = false, length = 10)
    private String contractNumber;

    public String getCustomerDocumentNumber() {
        return customerDocumentNumber;
    }

    public void setCustomerDocumentNumber(String customerDocumentNumber) {
        this.customerDocumentNumber = customerDocumentNumber;
    }

    public String getCompanyDocumentNumber() {
        return companyEntityDocumentNumber;
    }

    public void setCompanyDocumentNumber(String companyEntityDocumentNumber) {
        this.companyEntityDocumentNumber = companyEntityDocumentNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ContractId entity = (ContractId) o;
        return Objects.equals(this.customerDocumentNumber, entity.customerDocumentNumber) &&
                Objects.equals(this.contractNumber, entity.contractNumber) &&
                Objects.equals(this.companyEntityDocumentNumber, entity.companyEntityDocumentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerDocumentNumber, contractNumber, companyEntityDocumentNumber);
    }

}