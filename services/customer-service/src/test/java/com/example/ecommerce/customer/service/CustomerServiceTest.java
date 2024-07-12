package com.example.ecommerce.customer.service;

import com.example.ecommerce.customer.dto.AddressRequest;
import com.example.ecommerce.customer.dto.CustomerRequest;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.customer.entity.Address;
import com.example.ecommerce.customer.entity.Customer;
import com.example.ecommerce.customer.mapper.CustomerMapper;
import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest customerRequest;
    private Customer customer;
    private Address address;
    private CustomerResponse customerResponse;
    private List<Customer> customers;
    private CustomerResponse response1;
    private CustomerResponse response2;

    @BeforeEach
    void setUp() {
        AddressRequest addressRequest = new AddressRequest("Street", "City", "State", "Zipcode");

        customerRequest = new CustomerRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                addressRequest
        );

        address = Address.builder()
                .id(1)
                .street("Street")
                .city("City")
                .state("State")
                .zipCode("Zipcode")
                .customer(null)
                .build();

        customer = Customer.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(address)
                .build();

        address.setCustomer(customer);

        customerResponse = new CustomerResponse(
                "John",
                "Doe",
                "john.doe@example.com"
        );

        Customer customer1 = new Customer();
        customer1.setId(2);
        customer1.setFirstName("Jane");
        customer1.setLastName("Doe");
        customer1.setEmail("jane.doe@example.com");

        Customer customer2 = new Customer();
        customer2.setId(3);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setEmail("jane.smith@example.com");

        customers = Arrays.asList(customer1, customer2);

        response1 = new CustomerResponse("Jane", "Doe", "jane.doe@example.com");
        response2 = new CustomerResponse("Jane", "Smith", "jane.smith@example.com");
    }

    @Test
    public void CustomerService_CreateCustomer_ReturnsCustomerId() {
        when(customerMapper.toCustomer(any(CustomerRequest.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Integer customerId = customerService.createCustomer(customerRequest);

        assertNotNull(customerId);
        assertEquals(1, customerId);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void CustomerService_GetCustomerById_ReturnsCustomerResponse() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomer(any(Customer.class))).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomer(1);

        assertNotNull(response);
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("john.doe@example.com", response.email());
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    public void CustomerService_GetCustomer_ThrowsExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(1));
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    public void CustomerService_GetAllCustomers_ReturnsAllCustomers() {
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.fromCustomer(any(Customer.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        List<CustomerResponse> customerResponses = customerService.getAllCustomers();

        assertEquals(2, customerResponses.size());
        assertEquals(response1, customerResponses.get(0));
        assertEquals(response2, customerResponses.get(1));
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void CustomerService_DeleteCustomer_DeletesCustomer(){
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1);

        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).delete(any(Customer.class));
    }

    @Test
    public void CustomerService_DeleteCustomer_ThrowsExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1));
        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(0)).delete(any(Customer.class));
    }

    @Test
    public void CustomerService_UpdateCustomer_UpdatesCustomer(){
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toAddress(any(AddressRequest.class))).thenReturn(address);

        customerService.updateCustomer(1, customerRequest);

        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("Street", customer.getAddress().getStreet());
        assertEquals("City", customer.getAddress().getCity());
        assertEquals("State", customer.getAddress().getState());
        assertEquals("Zipcode", customer.getAddress().getZipCode());

        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void CustomerService_UpdateCustomer_ThrowsExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1, customerRequest));

        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(0)).save(any(Customer.class));
    }
}
