package com.example.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.dto.AssignAttributesRequest;
import com.example.model.AttributeValue;
import com.example.model.Product;
import com.example.model.ProductAttribute;
import com.example.repository.AttributeValueRepository;
import com.example.repository.ProductAttributeRepository;
import com.example.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductAttributeRepository productAttributeRepository;
    @Autowired private AttributeValueRepository attributeValueRepository;
    @Autowired private Cloudinary cloudinary;

    // ✅ Existing Add Product
    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestParam String name,
            @RequestParam Double price,
          
            @RequestParam MultipartFile image) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                ObjectUtils.asMap("folder", "ecommerce_products"));

        String imageUrl = (String) uploadResult.get("secure_url");

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        product.setImageUrl(imageUrl);

        productRepository.save(product);

        return ResponseEntity.ok("Product added successfully with Cloudinary image!");
    }

    // ✅ Existing Get All
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Get product with attributes and values
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductWithAttributes(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ResponseEntity.ok(product);
    }

    
    
    @PostMapping("/{id}/assign-attributes")
    public ResponseEntity<?> assignAttributes(
            @PathVariable Long id,
            @RequestBody AssignAttributesRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Clear existing attributes
        product.getAttributes().clear();

        // Add new attributes
        for (AssignAttributesRequest.AttributeDto attrDto : request.getAttributes()) {
            ProductAttribute attribute = new ProductAttribute(attrDto.getName(), product);

            for (String val : attrDto.getValues()) {
                AttributeValue value = new AttributeValue(val, attribute);
                attribute.getValues().add(value);
            }

            product.getAttributes().add(attribute);
        }

        productRepository.save(product);

        return ResponseEntity.ok(Map.of("message", "Attributes assigned successfully"));
    }
    

    
    @GetMapping("/colors/{productId}")
    public ResponseEntity<List<String>> getProductColors(@PathVariable Long productId) {
        return productRepository.findById(productId)
            .map(product -> {
                List<String> colors = product.getAttributes().stream()
                    .filter(attr -> "color".equalsIgnoreCase(attr.getName()))
                    .flatMap(attr -> attr.getValues().stream())
                    .map(AttributeValue::getValue)
                    .distinct()
                    .toList();
                return ResponseEntity.ok(colors);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    
    
    
    
}

