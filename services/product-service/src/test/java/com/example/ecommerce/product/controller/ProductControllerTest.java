package com.example.ecommerce.product.controller;

import com.example.ecommerce.product.dto.*;
import com.example.ecommerce.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private ProductPurchaseRequest productPurchaseRequest;
    private ProductPurchaseResponse productPurchaseResponse;
    private List<ProductResponse> productResponseList;
    private List<ProductPurchaseResponse> productPurchaseResponseList;
    private List<ProductAvailabilityResponse> productAvailabilityResponseList;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest(
                "Product1",
                "Description1",
                10.0,
                BigDecimal.valueOf(1234),
                1
        );

        productResponse = new ProductResponse(
                1,
                "Product1",
                "Description1",
                10.0,
                BigDecimal.valueOf(1234),
                1,
                "Category",
                "Category Description"
        );

        productPurchaseRequest = new ProductPurchaseRequest(1, 5.0);
        productPurchaseResponse = new ProductPurchaseResponse(
                1,
                "Product1",
                "Description1",
                BigDecimal.valueOf(1234),
                5.0

        );

        productResponseList = List.of(productResponse);
        productPurchaseResponseList = List.of(productPurchaseResponse);

        ProductAvailabilityResponse productAvailabilityResponse1 = new ProductAvailabilityResponse(
                1,
                "Product1",
                "Description1",
                BigDecimal.valueOf(1234),
                10.0
        );

        ProductAvailabilityResponse productAvailabilityResponse2 = new ProductAvailabilityResponse(
                2,
                "Product2",
                "Description2",
                BigDecimal.valueOf(1234),
                10.0
        );

        productAvailabilityResponseList = List.of(productAvailabilityResponse1, productAvailabilityResponse2);
    }

    @Test
    public void ProductController_CreateProduct_ReturnsProductId() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));

        Mockito.verify(productService, Mockito.times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    public void ProductController_PurchaseProduct_ReturnsProductPurchaseResponse() throws Exception {
        when(productService.purchaseProducts(anyList())).thenReturn(productPurchaseResponseList);

        mockMvc.perform(post("/api/v1/products/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(productPurchaseRequest))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(productPurchaseResponseList.get(0).id())))
                .andExpect(jsonPath("$[0].name", is(productPurchaseResponseList.get(0).name())));

        Mockito.verify(productService, Mockito.times(1)).purchaseProducts(anyList());
    }

    @Test
    public void ProductController_CheckAvailability_ReturnsProductAvailabilityResponse() throws Exception {
        when(productService.checkProductAvailability(anyList(), anyList())).thenReturn(productAvailabilityResponseList);

        mockMvc.perform(get("/api/v1/products/check-availability")
                        .param("productIds", "1, 2")
                        .param("quantities", "5.0, 3.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].name", is("Product1")))
                .andExpect(jsonPath("$[1].productId", is(2)))
                .andExpect(jsonPath("$[1].name", is("Product2")));

        Mockito.verify(productService, Mockito.times(1)).checkProductAvailability(anyList(), anyList());
    }

    @Test
    public void ProductController_FindById_ReturnsProductResponse() throws Exception {
        when(productService.findById(1)).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/{product-id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product1")));

        Mockito.verify(productService, Mockito.times(1)).findById(1);
    }

    @Test
    public void ProductController_FindAll_ReturnsAllProducts() throws Exception {
        when(productService.findAll()).thenReturn(productResponseList);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Product1")));

        Mockito.verify(productService, Mockito.times(1)).findAll();
    }
}
