//this controller handles the operations listed below
//deposit , withdraw , mini statement or all the transactions display

package com.bank.unitedbank.controller;

import com.bank.unitedbank.dto.response.AccountDataDTO;
import com.bank.unitedbank.dto.response.CustomerDataDTO;
import com.bank.unitedbank.dto.response.TransactionDataDTO;
import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.exception.UnauthorizedAccountException;
import com.bank.unitedbank.mapper.DataResponseMapper;
import com.bank.unitedbank.mapper.TransactionResponseMapper;
import com.bank.unitedbank.service.TransactionService;
import com.bank.unitedbank.service.CustomerService;
import com.bank.unitedbank.service.AccountService;
import com.bank.unitedbank.entity.Customer;


import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class DashboardController {
	
	private final TransactionService transactionService;
	private final CustomerService customerService;
	private final AccountService accountService;
	
	public DashboardController(
			TransactionService tService,
			CustomerService cService,
			AccountService aService
	) {
		this.transactionService = tService;
		this.customerService = cService;
		this.accountService = aService;
	}
	
	@GetMapping("/detail")
	public ResponseEntity<?> showDashboard(){

		//extract the customerId from the token sent by user
		//customer is sure to have token if request reached this far....
		Long customerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//fetches the customer entity
		Customer customer = customerService.getCustomerById(customerId);
		//we need not pass the password and pin to the user so
		//convert the Customer to a DTO object

		CustomerDataDTO customerDTO = DataResponseMapper.toCustomerResponseDTO(customer);

		return new ResponseEntity<>(customerDTO , HttpStatus.OK);

	}

	@GetMapping("/account/{accNo}")
	public ResponseEntity<?> getAccountDetails(@NotNull @PathVariable Long accNo){

		//JWT validation and ownerShip validation remains

		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		if(!customerService.isOwnerOfAccount(customerId , accNo)){
			throw new UnauthorizedAccountException("The requested details are unauthorized.");
		}

		Account account = accountService.getAccountById(accNo);

		//get the transaction Data DTO
		List<TransactionDataDTO> transactionDataDTOList =
				TransactionResponseMapper.toListOfTransactionResponseDTO(
						transactionService.getMiniStatement(accNo)
				);

		//Build a response by both account and transaction details
		AccountDataDTO accountDataDTO =
				DataResponseMapper.toAccountTransactionDTO(account,transactionDataDTOList);

		return new ResponseEntity<>(accountDataDTO , HttpStatus.OK);

	}
	
	@GetMapping("/mtransactions/{accNo}")
	public ResponseEntity<?> getMonthlyTransaction(
			@NotNull @PathVariable Long accNo,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
	) {
		
		Long customerId = (Long) SecurityContextHolder.getContext()
								.getAuthentication().getPrincipal();

		if(!customerService.isOwnerOfAccount(customerId, accNo)) {
			throw new UnauthorizedAccountException("The requested details are unauthorized.");
		}

		LocalDateTime startDT = startDate.atStartOfDay();
		LocalDateTime endDT = endDate.atTime(LocalTime.MAX);

		List<TransactionDataDTO> transactionDataDTOList
				= TransactionResponseMapper.toListOfTransactionResponseDTO(
						transactionService.getMonthStatement(accNo ,
								startDT.atZone(ZoneId.systemDefault()).toInstant() ,
								endDT.atZone(ZoneId.systemDefault()).toInstant()
						)
					);


			
		return new ResponseEntity<>( transactionDataDTOList , HttpStatus.OK);

	}

}
