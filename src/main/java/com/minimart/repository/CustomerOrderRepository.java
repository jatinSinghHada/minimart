package com.minimart.repository;

import com.minimart.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

}
