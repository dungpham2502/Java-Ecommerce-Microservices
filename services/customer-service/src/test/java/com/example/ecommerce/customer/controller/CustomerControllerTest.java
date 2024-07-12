package com.example.ecommerce.customer.controller;

import com.example.ecommerce.customer.dto.AddressRequest;
import com.example.ecommerce.customer.dto.CustomerRequest;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.customer.entity.Customer;
import com.example.ecommerce.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;
    private List<CustomerResponse> customerResponseList;

    @BeforeEach
    void setUp() {
        customerRequest = new CustomerRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                new AddressRequest("Street", "City", "State", "Zipcode")
        );

        customerResponse = new CustomerResponse(
                "John",
                "Doe",
                "john.doe@example.com"
        );

        CustomerResponse customerResponse1 = new CustomerResponse(
                "John1",
                "Doe1",
                "john.doe1@example.com"
        );

        CustomerResponse customerResponse2 = new CustomerResponse(
                "John2",
                "Doe2",
                "john.doe2@example.com"
        );

        customerResponseList = new ArrayList<>();
        customerResponseList.add(customerResponse1);
        customerResponseList.add(customerResponse2);
    }

    @Test
    public void CustomerController_CreateCustomer_ReturnsCustomerId() throws Exception {
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));

        verify(customerService, times(1)).createCustomer(any(CustomerRequest.class));
    }

    @Test
    public void CustomerController_GetCustomerById_ReturnsCustomerId() throws Exception {
        int customerId = 1;
        when(customerService.getCustomer(customerId)).thenReturn(customerResponse);

        mockMvc.perform(get("/api/v1/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(customerService, times(1)).getCustomer(customerId);
    }

    @Test
    public void CustomerController_GetAllCustomers_ReturnsAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customerResponseList);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John1")))
                .andExpect(jsonPath("$[0].lastName", is("Doe1")))
                .andExpect(jsonPath("$[0].email", is("john.doe1@example.com")))
                .andExpect(jsonPath("$[1].firstName", is("John2")))
                .andExpect(jsonPath("$[1].lastName", is("Doe2")))
                .andExpect(jsonPath("$[1].email", is("john.doe2@example.com")));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void CustomerController_UpdateCustomer_UpdatesCustomer() throws Exception {
        int customerId = 1;
        when(customerService.updateCustomer(customerId, customerRequest)).thenReturn(customerResponse);

        mockMvc.perform(put("/api/v1/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
        verify(customerService, times(1)).updateCustomer(customerId, customerRequest);
    }

    @Test
    public void CustomerController_DeleteCustomer_DeletesCustomer() throws Exception {
        int customerId = 1;
        doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/api/v1/customers/{customerId}", customerId))
                .andExpect(status().isAccepted());

        verify(customerService, times(1)).deleteCustomer(customerId);
    }
}
