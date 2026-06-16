//this controller handles the operations listed below
//deposit , withdraw , mini statement or all the transactions display



package com.bank.unitedbank.controller;

import com.bank.unitedbank.dto.CustomerDAtaDTO;
import com.bank.unitedbank.dto.response.TransactionDataDTO;
import com.bank.unitedbank.service.TransactionService;
import com.bank.unitedbank.entity.Transaction;
import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.service.CustomerService;
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

			CustomerDAtaDTO customerDTO = CustomerDAtaDTO.convertCustomerToCRDTO(customer);


			model.addAttribute("customerProfile" , customerDTO);
			
			List<Transaction> transactions = transactionService.getMiniStatement(accNo);

			//below code converts the list of transactions to the list of DTO object
			List<TransactionDataDTO> transactionsDTO = transactions.stream()
					.map(TransactionDataDTO::convertTransactionToTRDTO)
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
			

			CustomerDAtaDTO customerResponseDTO =
					CustomerDAtaDTO.convertCustomerToCRDTO(customerService.getCustomerById(accNo));

			model.addAttribute("customerProfile" , customerResponseDTO);
			
			List<Transaction> allTransactions = transactionService.getAllStatement(accNo);

			//below code converts the list of transactions to the list of DTO object
			List<TransactionDataDTO> transactionsDTO = allTransactions.stream()
					.map(TransactionDataDTO::convertTransactionToTRDTO)
					.toList();

			model.addAttribute("allTransactions" , transactionsDTO);
			
			return "fullStatementPage";
			
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error" , e.getMessage());
			return "redirect:/dashboard";
		}
	}

//	@GetMapping("/filterTransactionOnDate")
//	public String getTransactionsByMonth(
//			@RequestParam LocalDate startDate ,
//			@RequestParam LocalDate endDate,
//			HttpSession session, Model model,
//			RedirectAttributes redirectAttributes
//	){
//
//		try{
//
//			Integer accNo = (Integer) session.getAttribute("accNo");
//
//			if(accNo == null){
//				return "redirect:/login";
//			}
//
//			Customer customer = customerService.getCustomerById(accNo);
//
//			CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.convertCustomerToCRDTO(customer);
//
//			model.addAttribute("customerProfile" , customerResponseDTO);
//
//			//converting the LocalDate to LocalDateTime range for the service
//			//as we have stored LocalDateTime in DB
//
//			LocalDateTime startDateTime = startDate.atStartOfDay();
//			LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
//
//			List<Transaction> transactions = transactionService.getMonthStatement(accNo, startDateTime , endDateTime);
//
//			List<TransactionResponseDTO> transactionsDTO = transactions.stream()
//					.map(TransactionResponseDTO::convertTransactionToTRDTO)
//					.toList();
//
//			model.addAttribute("transactionsByMonth" , transactionsDTO);
//
//			return "monthStatementPage";
//
//		}catch(RuntimeException e){
//			redirectAttributes.addFlashAttribute("error" , e.getMessage());
//			return "redirect:/dashboard";
//		}
//
//	}

}
