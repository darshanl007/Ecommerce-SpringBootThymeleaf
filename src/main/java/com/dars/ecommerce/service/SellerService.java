package com.dars.ecommerce.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.dars.ecommerce.dto.Product;
import com.dars.ecommerce.dto.Seller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

public interface SellerService {

	String loadRegister(ModelMap map);

	String loadRegister(@Valid Seller seller, BindingResult result, HttpSession session);

	String submitOtp(int id, int otp, HttpSession session);

	String loadHome(HttpSession session);

	String addProduct(HttpSession session, ModelMap map);

	String addProduct(HttpSession session, @Valid Product product, BindingResult result, MultipartFile image);

	String viewProducts(HttpSession session, ModelMap map);

}
