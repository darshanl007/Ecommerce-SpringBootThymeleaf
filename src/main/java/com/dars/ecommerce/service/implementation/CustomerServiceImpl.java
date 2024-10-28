package com.dars.ecommerce.service.implementation;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.dars.ecommerce.dto.Customer;
import com.dars.ecommerce.dto.Product;
import com.dars.ecommerce.helper.AES;
import com.dars.ecommerce.helper.MyEmailSender;
import com.dars.ecommerce.repository.CustomerRepository;
import com.dars.ecommerce.repository.ProductRepository;
import com.dars.ecommerce.repository.SellerRepository;
import com.dars.ecommerce.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	Customer customer;

	@Autowired
	MyEmailSender emailSender;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	ProductRepository productRepository;

	@Override
	public String loadRegister(ModelMap map) {
		map.put("customer", customer);
		return "customer-register.html";
	}

	@Override
	public String loadRegister(@Valid Customer customer, BindingResult result, HttpSession session) {
		if (!customer.getPassword().equals(customer.getConfirmpassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "* Password Missmatch");
		if (customerRepository.existsByEmail(customer.getEmail())
				|| sellerRepository.existsByEmail(customer.getEmail()))
			result.rejectValue("email", "error.email", "* Email should be Unique");
		if (customerRepository.existsByMobile(customer.getMobile())
				|| sellerRepository.existsByMobile(customer.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number should be Unique");

		if (result.hasErrors())
			return "customer-register.html";
		else {
			int otp = new Random().nextInt(100000, 1000000);
			customer.setOtp(otp);
			customer.setPassword(AES.encrypt(customer.getPassword(), "123"));
			customerRepository.save(customer);
			emailSender.sendOtp(customer);

			session.setAttribute("success", "Otp Sent Success");
			session.setAttribute("id", customer.getId());
			return "redirect:/customer/otp";
		}
	}

	@Override
	public String submitOtp(int id, int otp, HttpSession session) {
		Customer customer = customerRepository.findById(id).orElseThrow();
		if (customer.getOtp() == otp) {
			customer.setVerified(true);
			customerRepository.save(customer);
			session.setAttribute("success", "Account Created Success");
			return "redirect:/";
		} else {
			session.setAttribute("failure", "Invalid OTP");
			session.setAttribute("id", customer.getId());
			return "redirect:/customer/otp";
		}
	}

	@Override
	public String loadHome(HttpSession session) {
		if (session.getAttribute("customer") != null)
			return "customer-home.html";
		else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}

	@Override
	public String viewProducts(HttpSession session, ModelMap map) {
		if (session.getAttribute("customer") != null) {

			List<Product> products = productRepository.findByApprovedTrue();
			if (products.isEmpty()) {
				session.setAttribute("failure", "No Products Found");
				return "redirect:/customer/home";
			} else {
				map.put("products", products);
				return "customer-products.html";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}
}