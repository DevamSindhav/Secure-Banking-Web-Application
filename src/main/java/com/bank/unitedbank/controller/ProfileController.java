package com.bank.unitedbank.controller;

import com.bank.unitedbank.dto.request.DeleteAccountDTO;
import com.bank.unitedbank.dto.request.DeleteCustomerDTO;
import com.bank.unitedbank.dto.request.UpdatePinDTO;
import com.bank.unitedbank.dto.request.UpdatePasswordDTO;
import com.bank.unitedbank.exception.UnauthorizedAccountException;
import com.bank.unitedbank.service.CustomerService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
public class ProfileController {

	private final CustomerService customerService;

	public ProfileController(CustomerService cService) {
		
		this.customerService = cService;
	}

	
	@PutMapping("/password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody UpdatePasswordDTO upPassDTO) {

		Long customerId =
				(Long) SecurityContextHolder.getContext()
								.getAuthentication().getPrincipal();

		Long typedId = upPassDTO.getCustomerId();

		if(!typedId.equals(customerId)){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}

		customerService.passwordUpdate(
				upPassDTO.getCustomerId(),
				upPassDTO.getOldPassword(),
				upPassDTO.getNewPassword()
		);
		return new ResponseEntity<>( "Password updated successfully!", HttpStatus.OK);
	}
		
	@PutMapping("/pin")
	public ResponseEntity<?> showUpdatePinPage(@Valid @RequestBody UpdatePinDTO upPinDTO) {

		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		Long typedId = upPinDTO.getCustomerId();

		if(!typedId.equals(customerId)){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}

		customerService.updateAccountPin(
				upPinDTO.getCustomerId(),
				upPinDTO.getAccNo(),
				upPinDTO.getPassword(),
				upPinDTO.getNewPin()
		);
		return new ResponseEntity<>( "Pin updated successfully!", HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/account")
	public ResponseEntity<?> deleteAccount(@Valid @RequestBody DeleteAccountDTO deleteAccountDTO) {

		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		Long typedId = deleteAccountDTO.getCustomerId();

		if(!typedId.equals(customerId)){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}

		customerService.deleteAccount(
				deleteAccountDTO.getCustomerId(),
				deleteAccountDTO.getAccNo(),
				deleteAccountDTO.getPassword()
		);

		return new ResponseEntity<>("Account deleted successfully!" , HttpStatus.OK);
	}

	@DeleteMapping("/delete/customer")
	public ResponseEntity<?> deleteCustomer(@Valid @RequestBody DeleteCustomerDTO deleteCustomerDTO){

		Long customerId =
				(Long) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

		Long typedId = deleteCustomerDTO.getCustomerId();

		if(!typedId.equals(customerId)){
			throw new UnauthorizedAccountException("Unauthorized access!");
		}

		customerService.deleteCustomer(
				deleteCustomerDTO.getCustomerId(),
				deleteCustomerDTO.getPassword()
		);

		return new ResponseEntity<>("Customer account deleted successfully!" , HttpStatus.OK);
	}
}