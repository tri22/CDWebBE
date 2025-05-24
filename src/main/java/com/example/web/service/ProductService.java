package com.example.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.web.entity.Product;
import com.example.web.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setPdName(productDetails.getPdName());
            product.setPdPrice(productDetails.getPdPrice());
            product.setPdImage(productDetails.getPdImage());
            product.setPdCategory(productDetails.getPdCategory());
            product.setPdDescribe(productDetails.getPdDescribe());
            product.setPdColor(productDetails.getPdColor());
            product.setPdRating(productDetails.getPdRating());
            return productRepository.save(product);
        });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
