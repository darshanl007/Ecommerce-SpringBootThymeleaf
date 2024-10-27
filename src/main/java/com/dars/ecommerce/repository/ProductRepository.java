package com.dars.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dars.ecommerce.dto.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
