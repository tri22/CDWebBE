package com.example.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.web.dto.request.ProductRequest;
import com.example.web.dto.response.ProductResponse;
import com.example.web.entity.Product;
import com.example.web.mapper.IProductMapper;
import com.example.web.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productRepository.findByDeletedFalse();
    }

    public Product getProductById(Long id) {
        Optional<Product> product= productRepository.findById(id);
        return product.orElse(null);
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(product -> {
            productMapper.updateProduct(product, productRequest);
            return productMapper.toProductResponse(productRepository.save(product));
        });
    }

    public boolean deleteProduct(Long id) {
        try {
            Product product = productRepository.findById(id).orElseThrow();
            product.setDeleted(true);
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
