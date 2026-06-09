//this controller handles the operations listed below
//deposit , withdraw , mini statement or all the transactions display



package com.bank.Unitedbank.controller;

import com.bank.Unitedbank.dto.CustomerResponseDTO;
import com.bank.Unitedbank.dto.TransactionResponseDTO;
import com.bank.Unitedbank.service.TransactionService;
import com.bank.Unitedbank.entity.Transaction;
import com.bank.Unitedbank.entity.Customer;
import com.bank.Unitedbank.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DashboardController {
	
	private final TransactionService transactionService;
	private final CustomerService customerService;
	
	public DashboardController(TransactionService tService, CustomerService cService) {
		this.transactionService = tService;
		this.customerService = cService;
	}
	
	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session , RedirectAttributes redirectAttributes , Model model) {
		
		try {
			
			//this first fetches the data then we redirect this to the dasboardPage.jsp
			//which displays the fetched data
			
			Integer accNo = (Integer)(session.getAttribute("accNo"));
			
			if (accNo == null) {
	            return "redirect:/login"; 
	        }
			
			//fetches the customer entity
			Customer customer = customerService.getCustomerById(accNo);

			//we need not pass the password and pin to the user so
			//convert the Customer to a DTO object

			CustomerResponseDTO customerDTO = CustomerResponseDTO.convertCustomerToCRDTO(customer);


			model.addAttribute("customerProfile" , customerDTO);
			
			List<Transaction> transactions = transactionService.getMiniStatement(accNo);

			//below code converts the list of transactions to the list of DTO object
			List<TransactionResponseDTO> transactionsDTO = transactions.stream()
					.map(TransactionResponseDTO::convertTransactionToTRDTO)
					.toList();

			model.addAttribute("recentTransactions",transactionsDTO);
			
			return "dashboardPage";
			
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error" , e.getMessage());
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/allTransaction")
	public String getAllTransaction(HttpSession session ,RedirectAttributes redirectAttributes , Model model) {
		
		try {
			
			Integer accNo = (Integer)session.getAttribute("accNo");
			if(accNo == null) {
				return "redirect:/login";
			}
			

			CustomerResponseDTO customerResponseDTO =
					CustomerResponseDTO.convertCustomerToCRDTO(customerService.getCustomerById(accNo));

			model.addAttribute("customerProfile" , customerResponseDTO);
			
			List<Transaction> allTransactions = transactionService.getAllStatement(accNo);

			//below code converts the list of transactions to the list of DTO object
			List<TransactionResponseDTO> transactionsDTO = allTransactions.stream()
					.map(TransactionResponseDTO::convertTransactionToTRDTO)
					.toList();

			model.addAttribute("allTransactions" , transactionsDTO);
			
			return "fullStatementPage";
			
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error" , e.getMessage());
			return "redirect:/dashboard";
		}
	}

}
