package com.dars.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dars.ecommerce.dto.Product;
import com.dars.ecommerce.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	ProductRepository productRepository;

	@GetMapping("/home")
	public String loadHome(HttpSession session) {
		if (session.getAttribute("admin") != null)
			return "admin-home.html";
		else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}

	@GetMapping("/products")
	public String displayProducts(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			
			List<Product> products = productRepository.findAll();
			if (products.isEmpty()) {
				session.setAttribute("failure", "No Products Added Yet");
				return "redirect:admin/home";
			} else {
				map.put("products", products);
				return "admin-products.html";
			}
			
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}

}
