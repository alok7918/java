package com.example.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	  List<Product> findByNameContainingIgnoreCase(String name);
	  List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
	  
	  
	 
	  
}

