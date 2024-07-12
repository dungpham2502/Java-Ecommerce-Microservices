package com.example.ecommerce.product.service;

import com.example.ecommerce.category.Category;
import com.example.ecommerce.exception.ProductPurchaseException;
import com.example.ecommerce.product.dto.*;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.mapper.ProductMapper;
import com.example.ecommerce.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private Product product;
    private Category category;
    private List<Product> productList;
    private ProductPurchaseRequest productPurchaseRequest;
    private List<ProductPurchaseRequest> productPurchaseRequestList;
    private ProductPurchaseResponse productPurchaseResponse;
    private List<ProductPurchaseResponse> productPurchaseResponseList;
    private ProductAvailabilityResponse productAvailabilityResponse;
    List<ProductAvailabilityResponse> productAvailabilityResponseList;

    @BeforeEach
    void setup() {
        category = Category.builder()
                .id(1)
                .name("category_name")
                .description("category_description")
                .build();

        productRequest = new ProductRequest(
                "product1",
                "description1",
                10.0,
                BigDecimal.valueOf(1234),
                1
        );

        product = Product.builder()
                .id(1)
                .name("Product1")
                .description("Description1")
                .price(BigDecimal.valueOf(1234))
                .availableQuantity(10.0)
                .category(category)
                .build();

        productResponse = new ProductResponse(
                1,
                "Product1",
                "Description1",
                10.0,
                BigDecimal.valueOf(1234),
                1,
                "category_name",
                "category_description"
        );

        productPurchaseRequest = new ProductPurchaseRequest(1, 2.0);
        productPurchaseRequestList = List.of(productPurchaseRequest);
        productPurchaseResponse = new ProductPurchaseResponse(1, "Product1", "Description1", BigDecimal.valueOf(1234), 12.0);
        productPurchaseResponseList = List.of(productPurchaseResponse);
        productList = List.of(product);
        productAvailabilityResponse = new ProductAvailabilityResponse(1, "Product1", "Description1", BigDecimal.valueOf(1234), 10.0);
        productAvailabilityResponseList = List.of(productAvailabilityResponse);
    }

    @Test
    public void ProductService_CreateProduct_ReturnsProductId() {
        when(productMapper.toProduct(any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Integer productId = productService.createProduct(productRequest);

        assertNotNull(productId);
        assertEquals(1, productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void ProductService_FindProductById_ReturnsProductResponse() {
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1);

        assertNotNull(response);
        assertEquals("Product1", response.name());
        assertEquals(1, response.id());
        assertEquals("category_name", response.categoryName());
        assertEquals(1, response.categoryId());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    public void ProductService_FindById_ThrowsExceptionWhenNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.findById(1));
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    public void ProductService_FindAll_ReturnsAllProducts() {
        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);

        List<ProductResponse> responses = productService.findAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Product1", responses.get(0).name());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void ProductService_CheckProductAvailability_ReturnsAvailableProducts() {
        when(productRepository.findAllByIdInOrderById(anyList())).thenReturn(productList);
        when(productMapper.toProductAvailabilityResponse(any(Product.class))).thenReturn(productAvailabilityResponse);

        List<ProductAvailabilityResponse> responses = productService.checkProductAvailability(
                List.of(1), List.of(5.0));

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1, responses.get(0).productId());
        verify(productRepository, times(1)).findAllByIdInOrderById(anyList());
    }

    @Test
    public void ProductService_CheckProductAvailability_ThrowsExceptionWhenNotAvailable() {
        when(productRepository.findAllByIdInOrderById(anyList())).thenReturn(List.of());

        assertThrows(ProductPurchaseException.class, () ->
                productService.checkProductAvailability(List.of(1), List.of(5.0)));

        verify(productRepository, times(1)).findAllByIdInOrderById(anyList());
    }

    @Test
    public void ProductService_PurchaseProducts_ReturnsPurchasedProducts() {
        when(productRepository.findAllByIdInOrderById(anyList())).thenReturn(productList);
        when(productMapper.toProductPurchaseResponse(any(Product.class), anyDouble())).thenReturn(productPurchaseResponse);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        List<ProductPurchaseResponse> responses = productService.purchaseProducts(productPurchaseRequestList);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1, responses.get(0).id());
        verify(productRepository, times(1)).findAllByIdInOrderById(anyList());
    }

    @Test
    public void ProductService_PurchaseProducts_ThrowsExceptionWhenNotFound() {
        when(productRepository.findAllByIdInOrderById(anyList())).thenReturn(List.of());

        assertThrows(ProductPurchaseException.class, () ->
                productService.purchaseProducts(productPurchaseRequestList));

        verify(productRepository, times(1)).findAllByIdInOrderById(anyList());
    }
}
