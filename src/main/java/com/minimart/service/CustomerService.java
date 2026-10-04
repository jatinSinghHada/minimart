package com.minimart.service;

import com.minimart.dto.CustomerRequestDTO;
import com.minimart.dto.CustomerResponseDTO;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface CustomerService {

    CustomerResponseDTO addCustomer(CustomerRequestDTO requestDTO);

    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);

    Page<CustomerResponseDTO> getAllCustomers(int page, int size);

    CustomerResponseDTO getCustomerById(Long id);

    CustomerResponseDTO patchCustomer(Long id, Map<String, Object> updates);
}
