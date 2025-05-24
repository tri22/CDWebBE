package com.example.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.entity.Product;
import com.example.web.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService pdService;

    @GetMapping
    public List<Product> getAllProducts() {
        return pdService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return pdService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return pdService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Optional<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return pdService.updateProduct(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        boolean isDeleted = pdService.deleteProduct(id);
        return isDeleted ? "Product deleted successfully" : "Product not found";
    }
}
