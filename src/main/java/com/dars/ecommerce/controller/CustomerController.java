package com.dars.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dars.ecommerce.dto.Customer;
import com.dars.ecommerce.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;

	@GetMapping("/register")
	public String loadRegister(ModelMap map) {
		return customerService.loadRegister(map);
	}

	@PostMapping("/register")
	public String register(@Valid Customer customer, BindingResult result, HttpSession session) {
		return customerService.loadRegister(customer, result, session);
	}
	
	@GetMapping("/otp")
	public String loadOtpPage() {
		return "customer-otp.html";
	}

	@PostMapping("/submit-otp/{id}")
	public String submitOtp(@PathVariable int id, @RequestParam int otp, HttpSession session) {
		return customerService.submitOtp(id, otp, session);
	}
	
	@GetMapping("/home")
	public String loadHome(HttpSession session) {
		return customerService.loadHome(session);
	}
}
