package com.dars.ecommerce.service.implementation;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.dars.ecommerce.dto.Seller;
import com.dars.ecommerce.helper.AES;
import com.dars.ecommerce.helper.MyEmailSender;
import com.dars.ecommerce.repository.CustomerRepository;
import com.dars.ecommerce.repository.SellerRepository;
import com.dars.ecommerce.service.SellerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	Seller seller;

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	MyEmailSender emailSender;

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public String loadRegister(ModelMap map) {
		map.put("seller", seller);
		return "seller-register.html";
	}

	@Override
	public String loadRegister(Seller seller, BindingResult result, HttpSession session) {
		if (!seller.getPassword().equals(seller.getConfirmpassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "* Password Missmatch");
		if (customerRepository.existsByEmail(seller.getEmail()) || sellerRepository.existsByEmail(seller.getEmail()))
			result.rejectValue("email", "error.email", "* Email should be Unique");
		if (customerRepository.existsByMobile(seller.getMobile())
				|| sellerRepository.existsByMobile(seller.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number should be Unique");

		if (result.hasErrors())
			return "seller-register.html";
		else {
			int otp = new Random().nextInt(100000, 1000000);
			seller.setOtp(otp);
			seller.setPassword(AES.encrypt(seller.getPassword(), "123"));
			sellerRepository.save(seller);
			emailSender.sendOtp(seller);

			session.setAttribute("success", "Otp Sent Success");
			session.setAttribute("id", seller.getId());
			return "redirect:/seller/otp";
		}
	}

	@Override
	public String submitOtp(int id, int otp, HttpSession session) {
		Seller seller = sellerRepository.findById(id).orElseThrow();
		if (seller.getOtp() == otp) {
			seller.setVerified(true);
			sellerRepository.save(seller);
			session.setAttribute("success", "Account Created Success");
			return "redirect:/";
		} else {
			session.setAttribute("failure", "Invalid OTP");
			session.setAttribute("id", seller.getId());
			return "redirect:/seller/otp";
		}
	}

	@Override
	public String loadHome(HttpSession session) {
		if(session.getAttribute("seller")!=null) {
			return "seller-home.html";
		}
		else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}
}
