package com.dars.ecommerce.service.implementation;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.dars.ecommerce.dto.Seller;
import com.dars.ecommerce.helper.MyEmailSender;
import com.dars.ecommerce.repository.SellerRepository;
import com.dars.ecommerce.service.SellerService;

import jakarta.validation.Valid;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	Seller seller;

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	MyEmailSender emailSender;

	@Override
	public String loadRegister(ModelMap map) {
		map.put("seller", seller);
		return "seller-register.html";
	}

	@Override
	public String loadRegister(@Valid Seller seller, BindingResult result, ModelMap map) {
		if (!seller.getPassword().equals(seller.getConfirmpassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "* Password Missmatch");
		if (sellerRepository.existsByEmail(seller.getEmail()))
			result.rejectValue("email", "error.email", "* Email should be Unique");
		if (sellerRepository.existsByMobile(seller.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number should be Unique");

		if (result.hasErrors())
			return "seller-register.html";
		else {
			int otp = new Random().nextInt(100000, 1000000);
			seller.setOtp(otp);
			sellerRepository.save(seller);
			emailSender.sendOtp(seller);
			map.put("id", seller.getId());
			return "seller-otp.html";
		}

	}

	@Override
	public String submitOtp(int id, int otp, ModelMap map) {
		Seller seller = sellerRepository.findById(id).orElseThrow();
		if (seller.getOtp() == otp) {
			seller.setVerified(true);
			sellerRepository.save(seller);
			map.put("success", "Acoount Created Success");
			return "home.html";
		} else {
			map.put("failure", "Invalid OTP");
			map.put("id", seller.getId());
			return "seller-otp.html";

		}
	}
}
