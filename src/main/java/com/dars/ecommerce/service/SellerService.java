package com.dars.ecommerce.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.dars.ecommerce.dto.Seller;

import jakarta.validation.Valid;

public interface SellerService {

	String loadRegister(ModelMap map);

	String loadRegister(@Valid Seller seller, BindingResult result, ModelMap map);

	String submitOtp(int id, int otp, ModelMap map);

}
