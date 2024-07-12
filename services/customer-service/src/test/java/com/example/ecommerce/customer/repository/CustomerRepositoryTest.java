package com.example.ecommerce.customer.repository;

import com.example.ecommerce.customer.entity.Address;
import com.example.ecommerce.customer.entity.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void CustomerRepository_Saved_ReturnSavedCustomer() {
        //Arrange
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(
                        Address.builder()
                                .street("Street 1")
                                .city("City 1")
                                .state("State 1")
                                .zipCode("Zipcode1")
                                .build()
                )
                .build();
        //Act
        Customer savedCustomer = customerRepository.save(customer);

        //Assert
        Assertions.assertNotNull(savedCustomer);
        Assertions.assertTrue(savedCustomer.getId() > 0);
    }

    @Test
    public void CustomerRepository_findById_ReturnCustomer() {
        //Arrange
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(
                        Address.builder()
                                .street("Street 1")
                                .city("City 1")
                                .state("State 1")
                                .zipCode("Zipcode1")
                                .build()
                )
                .build();

        //Act
        Integer savedCustomerId = customerRepository.save(customer).getId();


        //Assert
        Assertions.assertNotNull(savedCustomerId);
        Assertions.assertTrue(savedCustomerId > 0);
        Assertions.assertTrue(customerRepository.findById(savedCustomerId).isPresent());
        Assertions.assertEquals(customer, customerRepository.findById(savedCustomerId).get());
    }

    @Test
    public void CustomerRepository_findAll_ReturnCustomers() {
        //Arrange
        Customer customer1 = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(
                        Address.builder()
                                .street("Street 1")
                                .city("City 1")
                                .state("State 1")
                                .zipCode("Zipcode1")
                                .build()
                )
                .build();

        Customer customer2 = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john2.doe@example.com")
                .address(
                        Address.builder()
                                .street("Street 1")
                                .city("City 1")
                                .state("State 1")
                                .zipCode("Zipcode1")
                                .build()
                )
                .build();


        //Act
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        List<Customer> customers = customerRepository.findAll();

        //Assert
        Assertions.assertNotNull(customers);
        Assertions.assertEquals(customers.size(), 2);
    }

    @Test
    public void CustomerRepository_UpdateCustomer_ReturnUpdatedCustomerNotNull() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(
                        Address.builder()
                                .street("Street 1")
                                .city("City 1")
                                .state("State 1")
                                .zipCode("Zipcode1")
                                .build()
                )
                .build();

        customerRepository.save(customer);

        Customer savedCustomer = customerRepository.findById(customer.getId()).get();
        savedCustomer.setFirstName("John1");
        savedCustomer.setLastName("Doe1");

        Customer updatedCustomer = customerRepository.save(savedCustomer);

        Assertions.assertNotNull(updatedCustomer.getFirstName());
        Assertions.assertEquals(updatedCustomer.getFirstName(), "John1");

        Assertions.assertNotNull(updatedCustomer.getLastName());
        Assertions.assertEquals(updatedCustomer.getLastName(), "Doe1");

    }

    @Test
    public void CustomerRepository_DeleteCustomer_ReturnDeletedCustomer() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address(
                        Address.builder()
                                .street("Street 1")
                                .city("City 1")
                                .state("State 1")
                                .zipCode("Zipcode1")
                                .build()
                )
                .build();

        customerRepository.save(customer);
        customerRepository.delete(customer);

        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());

        Assertions.assertEquals(empty(), customerOptional);
    }
}
