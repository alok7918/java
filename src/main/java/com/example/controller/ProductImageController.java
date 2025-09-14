package com.example.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.model.Product;
import com.example.model.ProductImage;
import com.example.repository.ProductImageRepository;
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
public class ProductImageController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private Cloudinary cloudinary; // configured as a @Bean

    /**
     * Upload images for a specific product color
     */
    @PostMapping("/{productId}/images")
    public ResponseEntity<?> uploadImagesForColor(
            @PathVariable Long productId,
            @RequestParam("colorName") String colorName,
            @RequestParam("images") List<MultipartFile> images
    ) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (MultipartFile file : images) {
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();

            // Save in DB
            ProductImage pi = new ProductImage();
            pi.setProduct(product);
            pi.setColorName(colorName);
            pi.setImageUrl(imageUrl);

            productImageRepository.save(pi);
        }

        return ResponseEntity.ok("Images uploaded successfully for color: " + colorName);
    }

    
    
 // ProductController.java

    @GetMapping("/{productId}/images-by-color")
    public ResponseEntity<?> getImagesByColor(@PathVariable Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Get all color-specific images
        List<Map<String, String>> imagesByColor = productImageRepository.findByProductId(productId)
                .stream()
                .map(pi -> Map.of(
                        "color", pi.getColorName(),
                        "imageUrl", pi.getImageUrl()
                ))
                .toList();

        // Always add default/general image at first position
        List<Map<String, String>> updatedList = new java.util.ArrayList<>();
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            updatedList.add(Map.of(
                    "color", "default",
                    "imageUrl", product.getImageUrl()
            ));
        }
        updatedList.addAll(imagesByColor);

        return ResponseEntity.ok(updatedList);
    }

    
    
    
    
    
    

}