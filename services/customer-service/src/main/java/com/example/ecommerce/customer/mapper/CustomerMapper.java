package com.example.ecommerce.customer.mapper;

import com.example.ecommerce.customer.dto.AddressRequest;
import com.example.ecommerce.customer.dto.CustomerRequest;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.customer.entity.Address;
import com.example.ecommerce.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Address toAddress(AddressRequest addressRequest) {
        return Address.builder()
                .id(null)
                .street(addressRequest.street())
                .city(addressRequest.city())
                .state(addressRequest.state())
                .zipCode(addressRequest.zipCode())
                .customer(null)
                .build();
    }

    public Customer toCustomer(CustomerRequest request){
        if (request == null){
            return null;
        }
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .address(toAddress(request.address()))
                .build();
        customer.getAddress().setCustomer(customer);
        return customer;
    }

    public CustomerResponse fromCustomer(Customer customer){
        if (customer == null){
            return null;
        }

        return new CustomerResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
    }
}
