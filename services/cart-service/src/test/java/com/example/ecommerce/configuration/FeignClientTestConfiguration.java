package com.example.ecommerce.configuration;

import com.example.ecommerce.customer.client.CustomerClient;
import com.example.ecommerce.product.client.ProductClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class FeignClientTestConfiguration {

    @Bean
    @Primary
    public CustomerClient customerClient() {
        return Mockito.mock(CustomerClient.class);
    }

    @Bean
    @Primary
    public ProductClient productClient() {
        return Mockito.mock(ProductClient.class);
    }
}
