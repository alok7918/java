package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g. Color, Size

    @ManyToOne
    @JoinColumn(name = "product_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Product product;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<AttributeValue> values = new ArrayList<>();

    public ProductAttribute() {}

    public ProductAttribute(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public List<AttributeValue> getValues() { return values; }
    public void setValues(List<AttributeValue> values) { this.values = values; }
}
