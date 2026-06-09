//this controller handles all the public action a user can take 
//like login, logout , or register etc.


package com.bank.Unitedbank.controller;

import com.bank.Unitedbank.entity.Customer;
import com.bank.Unitedbank.dto.CustomerResponseDTO;
import com.bank.Unitedbank.service.CustomerService;
import com.bank.Unitedbank.dto.CustomerRegisterDTO;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;


@Controller
public class AuthorizationController{
	
	private final CustomerService customerService;
	
	public AuthorizationController(CustomerService customerService) {
		
		this.customerService = customerService;
	}
	
	@GetMapping("/")
	public String showIndexPage() {
	    return "indexPage"; 
	}
	
	@GetMapping("/register")
	public String showRegistration(Model model) {
		
		model.addAttribute("customerDTO" , new CustomerRegisterDTO());
		return "registerPage";
		
	}
	
	@PostMapping("/register")
	public String registrationProcess(@ModelAttribute CustomerRegisterDTO customerRegisterDTO,
										RedirectAttributes redirectAttributes ,Model model) {
		
		try {

			//Convert the DTO fields in to a real Customer object

			Customer realCustomer = CustomerRegisterDTO.convertCRegDTOtoCustomer(customerRegisterDTO);
			

			//success
			customerService.registerCustomer(realCustomer);
			redirectAttributes.addAttribute("registeredUser", true);
			return "redirect:/login";
		
		}catch(RuntimeException e) {
			
			//Failed Registration
			model.addAttribute("error" , e.getMessage());
			return "registerPage";
		}
		
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "loginPage";
	}
	
	@PostMapping("/processlogin")
	public String loginProcess(@RequestParam String email ,@RequestParam String password, 
								HttpSession session, Model model) {
		
		try {
			
			//verification of user
			Customer validCustomer = customerService.loginValidation(email, password);
			
			//session attribute setup
			//only saved the accNo
			//as storing whole customer will raise security as well as performance concerns
			session.setAttribute("accNo", validCustomer.getAccNo());
			
			return "redirect:/dashboard";
		}catch(RuntimeException e) {
			
			model.addAttribute("error" , e.getMessage());
			return "loginPage";
		}
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		//need to invalidate the current session when logging out
		//only redirecting to the index page is not real logout
		session.invalidate();
		
		return "redirect:/";
	}
	
}
