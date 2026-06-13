package com.bank.unitedbank.controller;


import com.bank.unitedbank.service.TransactionService;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionController {

	private final TransactionService transactionService;
	
	public TransactionController(TransactionService tService) {
		this.transactionService = tService;
	}
	
	@GetMapping("/withdraw")
	public String showWithdrawPage(HttpSession session ,RedirectAttributes redirectAttributes,
									Model model)	{
		Integer accNo = (Integer) session.getAttribute("accNo");
		
		if(accNo == null) {
			return "redirect:/login";
		}
		
		return "withdrawPage";
	}
	
	@PostMapping("/processWithdraw")
	public String withdrawProcess(@RequestParam BigDecimal amount , @RequestParam String pin ,
									HttpSession session , RedirectAttributes redirectAttribute, 
									Model model){
		try {
			Integer accNo = (Integer)session.getAttribute("accNo");
			
			if(accNo == null) {
				//not needed but just to be on safe side!!!
				return "redirect:/login";
			}
			
			transactionService.withdraw(accNo, pin, amount);
			
			redirectAttribute.addFlashAttribute("withdraw", true);
			return "redirect:/withdraw";
		}catch(RuntimeException e) {
			
			model.addAttribute("error" , e.getMessage());
			return "withdrawPage";
		}
	}
	
	@GetMapping("/deposit")
	public String showDepositPage(HttpSession session ,RedirectAttributes redirectAttributes,
									Model model)	{
		Integer accNo = (Integer) session.getAttribute("accNo");
		
		if(accNo == null) {
			return "redirect:/login";
		}
		
		return "depositPage";
	}
	
	@PostMapping("/processDeposit")
	public String depositProcess(@RequestParam BigDecimal amount ,
									HttpSession session , RedirectAttributes redirectAttribute, 
									Model model){
		try {
			Integer accNo = (Integer)session.getAttribute("accNo");
			
			if(accNo == null) {
				//not needed but just to be on safe side!!!
				return "redirect:/login";
			}
			
			transactionService.deposit(accNo,  amount);
			
			redirectAttribute.addFlashAttribute("deposit" , true);
			return "redirect:/deposit";
			
		}catch(RuntimeException e) {
			
			model.addAttribute("error" , e.getMessage());
			return "depositPage";
		}
	}
	
	@GetMapping("/transfer")
	public String showTransferPage(HttpSession session ,RedirectAttributes redirectAttributes,
									Model model)	{
		Integer accNo = (Integer) session.getAttribute("accNo");
		
		if(accNo == null) {
			return "redirect:/login";
		}
		
		return "transferPage";
	}
	
	@PostMapping("/processTransfer")
	public String transferProcess(@RequestParam Integer receiverAccNo,
									@RequestParam BigDecimal amount , @RequestParam String pin ,
									HttpSession session , RedirectAttributes redirectAttribute, 
									Model model){
		try {
			
			Integer senderAccNo = (Integer)session.getAttribute("accNo");
			
			if(senderAccNo == null) {
				//not needed but just to be on safe side!!!
				return "redirect:/login";
			}
			
			transactionService.transfer(senderAccNo, pin, receiverAccNo, amount);
			
			redirectAttribute.addFlashAttribute("transfer" , true);
			return "redirect:/transfer";
			
		}catch(RuntimeException e) {
			
			model.addAttribute("error" , e.getMessage());
			return "transferPage";
		}
	}
	
}

