package com.minimart.controller;

import com.minimart.dto.CustomerRequestDTO;
import com.minimart.dto.CustomerResponseDTO;
import com.minimart.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerResponseDTO addCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {

        return customerService.addCustomer(requestDTO);
    }

    @PutMapping("/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO requestDTO) {

        return customerService.updateCustomer(id, requestDTO);
    }

    @GetMapping
    public Page<CustomerResponseDTO> getAllCustomers(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        return customerService.getAllCustomers(page, size);
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable Long id) {

        return customerService.getCustomerById(id);
    }

    @PatchMapping("/{id}")
    public CustomerResponseDTO patchCustomer(@PathVariable Long id, @RequestBody Map<String, Object> updates) {

    return customerService.patchCustomer(id, updates);
}
}
