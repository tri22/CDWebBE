package com.example.web.controller;

import java.util.Date;
import java.util.List;

import com.example.web.configuration.JwtAuthenticationFilter;
import com.example.web.dto.request.LogRequest;
import com.example.web.entity.Product;
import com.example.web.entity.User;
import com.example.web.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return pdService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return pdService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest productRequest,
                                                         HttpServletRequest request) {
        User user = jwtAuthenticationFilter.extractUser(request);
        return pdService.updateProduct(id, productRequest)
                .map(product -> {
                    logService.addLog(LogRequest.builder()
                            .action("UPDATE_PRODUCT_SUCCESS")
                            .user(user)
                            .ip(request.getRemoteAddr())
                            .level("INFO")
                            .dataIn(productRequest)
                            .dataOut(product)
                            .date(new Date())
                            .resource("PRODUCT MANAGEMENT")
                            .build());
                    return ResponseEntity.ok(product);
                })
                .orElseGet(() -> {
                    logService.addLog(LogRequest.builder()
                            .action("UPDATE_PRODUCT_FAILED")
                            .user(user)
                            .ip(request.getRemoteAddr())
                            .level("WARN")
                            .dataIn(productRequest)
                            .dataOut("Product not found with id " + id)
                            .date(new Date())
                            .resource("PRODUCT MANAGEMENT")
                            .build());
                    return ResponseEntity.notFound().build();
                });
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                HttpServletRequest request) {
        User user = jwtAuthenticationFilter.extractUser(request);
        boolean isDeleted = pdService.deleteProduct(id);

        if (isDeleted) {
            logService.addLog(LogRequest.builder()
                    .action("DELETE_PRODUCT_SUCCESS")
                    .user(user)
                    .ip(request.getRemoteAddr())
                    .level("INFO")
                    .dataIn(id)
                    .dataOut("Deleted successfully")
                    .build());
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            logService.addLog(LogRequest.builder()
                    .action("DELETE_PRODUCT_FAILED")
                    .user(user)
                    .ip(request.getRemoteAddr())
                    .level("WARN")
                    .dataIn(id)
                    .dataOut("Product not found")
                    .build());
            return ResponseEntity.status(404).body("Product not found");
        }
    }


}
