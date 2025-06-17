package com.example.web.controller;

import java.util.List;

import com.example.web.configuration.JwtAuthenticationFilter;
import com.example.web.dto.response.ApiResponse;
import com.example.web.entity.Product;
import com.example.web.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.dto.request.ProductRequest;
import com.example.web.dto.response.ProductResponse;
import com.example.web.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService pdService;


    @Autowired
    private LogService logService;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @GetMapping
    public List<Product> getAllProducts() {
        return pdService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return pdService.getProductById(id);

    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return pdService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @RequestBody ProductRequest productRequest) {
        return pdService.updateProduct(id, productRequest).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        boolean isDeleted = pdService.deleteProduct(id);
        return isDeleted ? "Product deleted successfully" : "Product not found";
    }

}
