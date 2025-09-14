package com.example.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity

public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Double price;
 

  private String imageUrl; // Store Cloudinary image URL

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Double getPrice() {
	return price;
}

public void setPrice(Double price) {
	this.price = price;
}



public String getImageUrl() {
	return imageUrl;
}

public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}



@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
@com.fasterxml.jackson.annotation.JsonManagedReference
private List<ProductAttribute> attributes = new ArrayList<>();

public List<ProductAttribute> getAttributes() {
    return attributes;
}

public void setAttributes(List<ProductAttribute> attributes) {
    this.attributes = attributes;
}



@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
@com.fasterxml.jackson.annotation.JsonManagedReference
private List<ProductImage> images = new ArrayList<>();

public List<ProductImage> getImages() {
    return images;
}

public void setImages(List<ProductImage> images) {
    this.images = images;
}




}





