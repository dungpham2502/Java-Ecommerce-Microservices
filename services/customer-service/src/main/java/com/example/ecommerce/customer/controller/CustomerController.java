package com.example.ecommerce.customer.controller;


import com.example.ecommerce.customer.dto.CustomerRequest;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.customer.service.CustomerService;
import com.example.ecommerce.customer.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping
    public ResponseEntity<Integer> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ){
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerRequest request
    ) {
        CustomerResponse customerResponse = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        List<CustomerResponse> customerResponses = customerService.getAllCustomers();
        return ResponseEntity.ok(customerResponses);
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> checkIfExistById(
            @PathVariable("customer-id") Integer customerId
    ){
        return ResponseEntity.ok(customerService.checkIfExistById(customerId));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable("customer-id") Integer customerId
    ){
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable("customer-id") Integer customerId
    ){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }
}
