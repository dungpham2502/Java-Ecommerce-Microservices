package com.example.ecommerce.product.repository;

import com.example.ecommerce.category.Category;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.category.repository.CategoryRepository; // Import CategoryRepository
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();

        Category category = Category.builder()
                .name("Category")
                .description("Description")
                .products(new ArrayList<>())
                .build();

        // Save the category first
        Category savedCategory = categoryRepository.save(category);

        product1 = Product.builder()
                .name("Product1")
                .description("Description1")
                .availableQuantity(1.0)
                .price(BigDecimal.valueOf(1234))
                .category(savedCategory)
                .build();

        product2 = Product.builder()
                .name("Product2")
                .description("Description2")
                .availableQuantity(2.0)
                .price(BigDecimal.valueOf(5678))
                .category(savedCategory)
                .build();

        savedCategory.getProducts().add(product1);
        savedCategory.getProducts().add(product2);
    }

    @Test
    public void ProductRepository_Saved_ReturnSavedProduct() {
        Product savedProduct = productRepository.save(product1);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertNotNull(savedProduct.getId());
        Assertions.assertEquals(product1.getName(), savedProduct.getName());
    }

    @Test
    public void ProductRepository_FindById_ReturnProduct() {
        Integer productId = productRepository.save(product1).getId();

        Optional<Product> foundProduct = productRepository.findById(productId);

        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product1.getId(), foundProduct.get().getId());
    }

    @Test
    public void ProductRepository_UpdateProduct_ReturnUpdatedProduct() {
        productRepository.save(product1);

        product1.setName("Updated Product");
        product1.setPrice(BigDecimal.valueOf(5678));

        Product updatedProduct = productRepository.save(product1);

        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals("Updated Product", updatedProduct.getName());
        Assertions.assertEquals(BigDecimal.valueOf(5678), updatedProduct.getPrice());
    }

    @Test
    public void ProductRepository_DeleteProduct_RemovesProduct() {
        productRepository.save(product1);
        productRepository.deleteById(product1.getId());

        Optional<Product> foundProduct = productRepository.findById(product1.getId());

        Assertions.assertFalse(foundProduct.isPresent());
    }

    @Test
    public void ProductRepository_FindAll_ReturnsAllProducts() {
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.stream().anyMatch(p -> p.getId().equals(product1.getId())));
        Assertions.assertTrue(products.stream().anyMatch(p -> p.getId().equals(product2.getId())));
    }
}
