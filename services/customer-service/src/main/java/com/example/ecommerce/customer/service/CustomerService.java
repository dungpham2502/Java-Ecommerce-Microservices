package com.example.ecommerce.customer.service;

import com.example.ecommerce.customer.dto.CustomerRequest;
import com.example.ecommerce.customer.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    Integer createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Integer id, CustomerRequest request);
    CustomerResponse getCustomer(Integer id);
    List<CustomerResponse> getAllCustomers();
    void deleteCustomer(Integer id);
    Boolean checkIfExistById(Integer customerId);
}
