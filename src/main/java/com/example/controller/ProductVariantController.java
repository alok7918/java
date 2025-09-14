package com.example.controller;

import com.example.model.Product;
import com.example.model.ProductAttribute;
import com.example.model.ProductVariant;
import com.example.repository.ProductRepository;
import com.example.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/admin/products")
public class ProductVariantController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;
    
    
    @PersistenceContext
    private EntityManager entityManager; 

    // ✅ Create multiple variants for a product
    @PostMapping("/{productId}/variants")
    public ResponseEntity<?> createVariants(@PathVariable Long productId,
                                            @RequestBody List<Map<String, Object>> variantsData) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductVariant> savedVariants = new ArrayList<>();

        for (Map<String, Object> variantMap : variantsData) {
            // Extract attributes
            Map<String, String> attributes = new HashMap<>();
            for (Map.Entry<String, Object> entry : variantMap.entrySet()) {
                String key = entry.getKey();
                if (!key.equals("price") && !key.equals("stock")) {
                    attributes.put(key, entry.getValue() != null ? entry.getValue().toString() : "");
                }
            }

            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setAttributes(attributes);

            // Parse price
            if (variantMap.get("price") != null) {
                variant.setPrice(new BigDecimal(variantMap.get("price").toString()));
            }

            // Parse stock
            if (variantMap.get("stock") != null) {
                variant.setStock(Integer.parseInt(variantMap.get("stock").toString()));
            }

            savedVariants.add(variantRepository.save(variant));
        }

        return ResponseEntity.ok(savedVariants);
    }

    @GetMapping("/as/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id) {
        // Fetch product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Fetch attributes & values
        List<Object[]> attrRows = entityManager.createNativeQuery(
            "SELECT pa.id as attr_id, pa.name as attr_name, av.id as value_id, av.value as value " +
            "FROM product_attribute pa " +
            "JOIN attribute_value av ON av.attribute_id = pa.id " +
            "WHERE pa.product_id = :pid"
        )
        .setParameter("pid", id)
        .getResultList();

        Map<Long, Map<String, Object>> attributesMap = new LinkedHashMap<>();
        for (Object[] row : attrRows) {
            Long attrId = ((Number) row[0]).longValue();
            String attrName = (String) row[1];
            Long valId = ((Number) row[2]).longValue();
            String val = (String) row[3];

            attributesMap.putIfAbsent(attrId, new LinkedHashMap<>() {{
                put("id", attrId);
                put("name", attrName);
                put("values", new ArrayList<Map<String, Object>>());
            }});

            ((List<Map<String, Object>>) attributesMap.get(attrId).get("values"))
                    .add(Map.of("id", valId, "value", val));
        }

        // Fetch variants and their attributes
        List<Object[]> variantRows = entityManager.createNativeQuery(
            "SELECT pv.id as variant_id, pv.price, pv.stock, va.attribute_name, va.attribute_value " +
            "FROM product_variant pv " +
            "JOIN variant_attributes va ON va.variant_id = pv.id " +
            "WHERE pv.product_id = :pid"
        )
        .setParameter("pid", id)
        .getResultList();

        Map<Long, Map<String, Object>> variantsMap = new LinkedHashMap<>();
        for (Object[] row : variantRows) {
            Long variantId = ((Number) row[0]).longValue();
            Double price = ((Number) row[1]).doubleValue();
            Integer stock = ((Number) row[2]).intValue();
            String attrName = (String) row[3];
            String attrValue = (String) row[4];

            variantsMap.putIfAbsent(variantId, new LinkedHashMap<>() {{
                put("id", variantId);
                put("price", price);
                put("stock", stock);
                put("attributes", new LinkedHashMap<String, String>());
            }});

            ((Map<String, String>) variantsMap.get(variantId).get("attributes"))
                    .put(attrName, attrValue);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", product.getId());
        response.put("name", product.getName());
        response.put("attributes", new ArrayList<>(attributesMap.values()));
        response.put("variants", new ArrayList<>(variantsMap.values()));

        return ResponseEntity.ok(response);
    }

    
    
    
    
    
   
    
    
    
    
    
}
