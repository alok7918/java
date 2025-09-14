package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Product;
import com.example.repository.ProductRepository;


@RestController
@CrossOrigin
@RequestMapping("/user/products")
public class viewProduct {

	@Autowired
	  private ProductRepository productRepository;


	 @GetMapping
	public List<Product> getAllProducts() {
	    return productRepository.findAll();
	  }


	  @GetMapping("/{id}")
	  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
	      return productRepository.findById(id)
	              .map(ResponseEntity::ok)
	              .orElse(ResponseEntity.notFound().build());
	  }




	    @GetMapping("/search")
	    public List<Product> searchProducts(@RequestParam String name) {
	        return productRepository.findByNameContainingIgnoreCase(name);
	    }


	  @GetMapping("/filter")
	  public List<Product> filterByPrice(
	          @RequestParam Double minPrice,
	          @RequestParam Double maxPrice) {
	      return productRepository.findByPriceBetween(minPrice, maxPrice);
	  }

	    @GetMapping("/sort")
	    public List<Product> sortByName(@RequestParam(defaultValue = "asc") String order) {
	        Sort sort = Sort.by("name");
	        if (order.equalsIgnoreCase("desc")) {
	            sort = sort.descending();
	        }
	        return productRepository.findAll(sort);
	    }


}
