package com.example.ecommerce.product.service;

import com.example.ecommerce.exception.ProductPurchaseException;
import com.example.ecommerce.product.dto.*;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.mapper.ProductMapper;
import com.example.ecommerce.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products were not found");
        }

        var purchasedProducts = storedProducts.stream().map(storedProduct -> {
            var productRequest = request.stream()
                    .filter(r -> r.productId().equals(storedProduct.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductPurchaseException("Product ID not found in request: " + storedProduct.getId()));

            return updateAndSaveProduct(storedProduct, productRequest);
        }).toList();

        return purchasedProducts.stream()
                .map(product -> productMapper.toProductPurchaseResponse(product, product.getAvailableQuantity()))
                .collect(Collectors.toList());
    }

    private Product updateAndSaveProduct(Product storedProduct, ProductPurchaseRequest productPurchaseRequest) {
        if (storedProduct.getAvailableQuantity() < productPurchaseRequest.quantity()) {
            throw new ProductPurchaseException("Insufficient stock quantity for product ID " + storedProduct.getId());
        }
        storedProduct.setAvailableQuantity(storedProduct.getAvailableQuantity() - productPurchaseRequest.quantity());
        return productRepository.save(storedProduct);
    }

    @Override
    public List<ProductAvailabilityResponse> checkProductAvailability(List<Integer> productIds, List<Double> quantities) {
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products were not found");
        }

        return storedProducts.stream()
                .filter(storedProduct -> {
                    int index = productIds.indexOf(storedProduct.getId());
                    return storedProduct.getAvailableQuantity() >= quantities.get(index);
                })
                .map(productMapper::toProductAvailabilityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Integer createProduct(ProductRequest request) {
        var product = productMapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    @Override
    public ProductResponse findById(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + productId));
    }

    @Override
    public List<ProductResponse> findAll() {
        var products = productRepository.findAll();
        return products.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }
}
