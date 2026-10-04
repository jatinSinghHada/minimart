package com.minimart.service.impl;

import com.minimart.dto.CustomerRequestDTO;
import com.minimart.dto.CustomerResponseDTO;
import com.minimart.entity.Customer;
import com.minimart.repository.CustomerRepository;
import com.minimart.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.minimart.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponseDTO addCustomer(
            CustomerRequestDTO requestDTO) {

        Customer customer = Customer.builder()
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .build();

        Customer savedCustomer =
                customerRepository.save(customer);

        return mapToResponseDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(
            Long id,
            CustomerRequestDTO requestDTO) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        customer.setName(requestDTO.getName());
        customer.setPhone(requestDTO.getPhone());
        customer.setEmail(requestDTO.getEmail());

        Customer updatedCustomer =
                customerRepository.save(customer);

        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public Page<CustomerResponseDTO> getAllCustomers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return customerRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        return mapToResponseDTO(customer);
    }

    private CustomerResponseDTO mapToResponseDTO(
            Customer customer) {

        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .build();
    }

    @Override
    public CustomerResponseDTO patchCustomer(
            Long id,
            Map<String, Object> updates) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (updates.containsKey("name")) {
            customer.setName(
                    (String) updates.get("name"));
        }

        if (updates.containsKey("phone")) {
            customer.setPhone(
                    (String) updates.get("phone"));
        }

        if (updates.containsKey("email")) {
            customer.setEmail(
                    (String) updates.get("email"));
        }

        Customer updatedCustomer =
                customerRepository.save(customer);

        return mapToResponseDTO(updatedCustomer);
    }
}
