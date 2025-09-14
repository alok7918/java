package com.example.model;

import jakarta.persistence.*;

@Entity
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value; // e.g. Red, Blue

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private ProductAttribute attribute;

    public AttributeValue() {}

    public AttributeValue(String value, ProductAttribute attribute) {
        this.value = value;
        this.attribute = attribute;
    }

    public Long getId() { return id; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public ProductAttribute getAttribute() { return attribute; }
    public void setAttribute(ProductAttribute attribute) { this.attribute = attribute; }
}
