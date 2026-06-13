package com.bank.unitedbank.controller;

import com.bank.unitedbank.service.CustomerService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProfileController {

	private final CustomerService customerService;
	
	public ProfileController(CustomerService cService) {
		
		this.customerService = cService;
	}
	
	@GetMapping("/updatePassword")
	public String showUpdatePasswordPage(HttpSession session) {
		
		Integer accNo = (Integer) session.getAttribute("accNo");
		
		if(accNo == null) {
			return "redirect:/login";
		}
		
		return "updatePasswordPage";
	}
	
	@PostMapping("/processUpdatePassword")
	public String changePassword(@RequestParam String oldPass , @RequestParam String newPass ,
								HttpSession session , Model model ,
								RedirectAttributes redirectAttributes) {
		try {
			Integer accNo = (Integer) session.getAttribute("accNo");
			
			if(accNo == null) {
				return "redirect:/login";
			}
			
			customerService.passwordUpdate(accNo, oldPass, newPass);
			session.invalidate();
			redirectAttributes.addAttribute("passwordUpdated" , true);
			return "redirect:/login";
			
		}catch(RuntimeException e) {
			
			model.addAttribute("error" , e.getMessage());
			return "updatePasswordPage";
		}
		
	}
		
	@GetMapping("/updatePin")
	public String showUpdatePinPage(HttpSession session) {
			
		Integer accNo = (Integer) session.getAttribute("accNo");
			
		if(accNo == null) {
			return "redirect:/login";
		}
			
		return "updatePinPage";
	}
		
	@PostMapping("/processUpdatePin")
	public String changePin(@RequestParam String password , @RequestParam String newPin ,
							HttpSession session , Model model ,
							RedirectAttributes redirectAttributes) {
		try {
			Integer accNo = (Integer) session.getAttribute("accNo");
				
			if(accNo == null) {
				return "redirect:/login";
			}
				
			customerService.pinUpdate(accNo, password, newPin);
				
			redirectAttributes.addFlashAttribute("pinUpdated" , true);
			return "redirect:/updatePin";
				
		}catch(RuntimeException e) {
				
			model.addAttribute("error" , e.getMessage());
			return "updatePinPage";
		}
	}
	
	@GetMapping("/deleteAccount")
	public String showDeleteAccountPage(HttpSession session) {
			
		Integer accNo = (Integer) session.getAttribute("accNo");
			
		if(accNo == null) {
			return "redirect:/login";
		}
			
		return "deleteAccountPage";
	}
	
	@PostMapping("/processDeleteAccount")
	public String deleteAccount(@RequestParam String password , HttpSession session,
								Model model , RedirectAttributes redirectAttributes) {
		
		try {
			
			Integer accNo = (Integer) session.getAttribute("accNo");
			if(accNo == null) {
				return "redirect:/login";
			}
			
			customerService.deleteAccount(accNo, password);
			
			session.invalidate();
			redirectAttributes.addAttribute("accountDeleted" , true);
			return "redirect:/";
			
		}catch(RuntimeException e) {
			
			model.addAttribute("error" , e.getMessage());
			return "deleteAccountPage";
		}
		
		
	}
			
}