package com.example.api.integration.database.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PhoneId implements Serializable {
    private static final long serialVersionUID = 3099280508147233474L;
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "customer_document_number", nullable = false, length = 14)
    private String customerDocumentNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerDocumentNumber() {
        return customerDocumentNumber;
    }

    public void setCustomerDocumentNumber(String customerDocumentNumber) {
        this.customerDocumentNumber = customerDocumentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhoneId entity = (PhoneId) o;
        return Objects.equals(this.customerDocumentNumber, entity.customerDocumentNumber) &&
                Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerDocumentNumber, id);
    }

}