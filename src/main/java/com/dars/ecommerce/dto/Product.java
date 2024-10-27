package com.dars.ecommerce.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Component
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Size(min = 3, max = 15, message = "* Enter between 3~15 characters")
	private String name;
	@NotNull(message = "* Enter proper value")
	@DecimalMin(value = "48", message = "* Enter above 49rs")
	@DecimalMax(value = "100000", message = "* Enter below 1 lakh rs")
	private double price;
	@NotNull(message = "* Enter proper value")
	@Min(value = 1, message = "* Should be atleast one")
	@Max(value = 30, message = "* Maximum 30 is allowed")
	private int stock;
	@Size(min = 15, max = 100, message = "* Enter between 15-100 characters")
	private String description;
	private String imageLink;
	@NotNull(message = "* Enter something")
	private String category;
	private boolean approved;
	
	@ManyToOne
	Seller seller;

}
