package com.dars.ecommerce.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dars.ecommerce.dto.Customer;
import com.dars.ecommerce.dto.Seller;
import com.dars.ecommerce.helper.AES;
import com.dars.ecommerce.helper.MyEmailSender;
import com.dars.ecommerce.repository.CustomerRepository;
import com.dars.ecommerce.repository.SellerRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Value("${admin.email}")
	String adminEmail;

	@Value("${admin.password}")
	String adminPassword;

	@Autowired
	MyEmailSender emailSender;

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping("/")
	public String loadHome() {
		return "home.html";
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "login.html";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		if (email.equals(adminEmail) && password.equals(adminPassword)) {
			session.setAttribute("admin", "admin");
			session.setAttribute("success", "Login Success");
			return "redirect:/admin/home";
		} else {
			Seller seller = sellerRepository.findByEmail(email);
			Customer customer = customerRepository.findByEmail(email);

			if (seller == null && customer == null) {
				session.setAttribute("failure", "Invalid Email");
				return "redirect:/login";

			} else {
				if (seller == null) {
					if (AES.decrypt(customer.getPassword(), "123").equals(password)) {
						if (customer.isVerified()) {
							session.setAttribute("customer", customer);
							session.setAttribute("success", "Login Success");
							return "redirect:/customer/home";
						} else {
							customer.setOtp(new Random().nextInt(100000, 1000000));
							customerRepository.save(customer);
							emailSender.sendOtp(customer);
							session.setAttribute("success", "Otp Sent Success");
							session.setAttribute("id", customer.getId());
							return "redirect:/customer/otp";
						}
					} else {
						session.setAttribute("failure", "Invalid Passowrd");
						return "redirect:/login";
					}
				} else {
					if (AES.decrypt(seller.getPassword(), "123").equals(password)) {
						if (seller.isVerified()) {
							session.setAttribute("seller", seller);
							session.setAttribute("success", "Login Success");
							return "redirect:/seller/home";
						} else {
							seller.setOtp(new Random().nextInt(100000, 1000000));
							sellerRepository.save(seller);
							emailSender.sendOtp(seller);
							session.setAttribute("success", "Otp Sent Success");
							session.setAttribute("id", seller.getId());
							return "redirect:/seller/otp";
						}
					} else {
						session.setAttribute("failure", "Invalid Passowrd");
						return "redirect:/login";
					}
				}
			}
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		session.removeAttribute("seller");
		session.removeAttribute("customer");
		session.setAttribute("success", "Logged Out Successfully");
		return "redirect:/";
	}
}
