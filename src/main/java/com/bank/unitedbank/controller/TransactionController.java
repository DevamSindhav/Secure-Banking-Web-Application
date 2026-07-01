package com.bank.unitedbank.controller;

import com.bank.unitedbank.dto.request.WithdrawDTO;
import com.bank.unitedbank.dto.request.DepositDTO;
import com.bank.unitedbank.dto.request.TransferDTO;
import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.exception.UnauthorizedAccountException;
import com.bank.unitedbank.service.BankingService;
import com.bank.unitedbank.service.CustomerService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fund")
public class TransactionController {

	private final BankingService bankingService;
	private final CustomerService customerService;

	public TransactionController(BankingService bService , CustomerService cService) {
		this.bankingService = bService;
		this.customerService = cService;
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<?> withdrawProcess(@Valid @RequestBody WithdrawDTO withdrawDTO){
		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		if(!customerService.isOwnerOfAccount(customerId , withdrawDTO.getAccNo())){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}
		bankingService.withdraw(
				withdrawDTO.getAccNo(),
				withdrawDTO.getPin(),
				withdrawDTO.getAmount()
		);

		return new ResponseEntity<>("Withdraw successful!" , HttpStatus.OK);
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<?> depositProcess(@Valid @RequestBody DepositDTO depositDTO ){

		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		if(!customerService.isOwnerOfAccount(customerId , depositDTO.getAccNo())){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}

		bankingService.deposit(
				depositDTO.getAccNo(),
				depositDTO.getAmount()
		);
		return new ResponseEntity<>("Deposit successful!" , HttpStatus.OK);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<?> transferProcess(@Valid @RequestBody TransferDTO transferDTO){

		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		if(!customerService.isOwnerOfAccount(customerId , transferDTO.getSenderAccNo())){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}

		bankingService.transfer(
				transferDTO.getSenderAccNo(),
				transferDTO.getReceiverAccNo(),
				transferDTO.getPin(),
				transferDTO.getAmount()
		);

		return new ResponseEntity<>("Transfer successful!" , HttpStatus.OK);
	}
	
}

