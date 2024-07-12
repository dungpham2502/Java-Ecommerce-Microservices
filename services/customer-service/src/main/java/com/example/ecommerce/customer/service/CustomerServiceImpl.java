package com.example.ecommerce.customer.service;

import com.example.ecommerce.customer.mapper.CustomerMapper;
import com.example.ecommerce.customer.repository.AddressRepository;
import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.customer.dto.CustomerRequest;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.customer.entity.Customer;
import com.example.ecommerce.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper mapper, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
        this.addressRepository = addressRepository;
    }

    @Override
    public Integer createCustomer(CustomerRequest request) {
        var customer = customerRepository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    @Override
    public CustomerResponse updateCustomer(Integer id, CustomerRequest request) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Cannot update customer:: No customer found with provided ID:: %s", id)));
        mergeCustomer(customer, request);
        customerRepository.save(customer);
        return mapper.fromCustomer(customer);
    }

    @Override
    public CustomerResponse getCustomer(Integer id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with provided ID:: %s", id)));
        return mapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        var customers = customerRepository.findAll();
        return customers.stream().map(mapper::fromCustomer).toList();
    }

    @Override
    public void deleteCustomer(Integer id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Cannot delete customer:: No customer found with provided ID:: %s", id)));
        customerRepository.delete(customer);
    }

    @Override
    public Boolean checkIfExistById(Integer customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setAddress(mapper.toAddress(request.address()));
    }
}
