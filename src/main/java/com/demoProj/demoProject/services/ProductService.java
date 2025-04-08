package com.demoProj.demoProject.services;

import com.demoProj.demoProject.models.Product;
import com.demoProj.demoProject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public List<Product> getAllNonHiddenProducts() {
        return productRepository.findAllNonHidden();
    }
    public List<Product> getAllProducts() {
        return productRepository.findAllProducts();
    }

    public int addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    public int hideById(long id, boolean hidden) {
        return productRepository.hideById(id, hidden);
    }

//    public int deleteProduct(long id) {
//        return productRepository.deleteById(id);
//    }

    public Product updateProductQuantity(Long productId, int quantity) {
        return productRepository.updateProductId(productId, quantity);
    }

    public int updateProduct(long id, String productName,double price, String description) {
        return productRepository.updateProduct(id, productName, price, description);
    }

    public Product getProductById(long id) {
        return productRepository.findById(id);
    }
}
