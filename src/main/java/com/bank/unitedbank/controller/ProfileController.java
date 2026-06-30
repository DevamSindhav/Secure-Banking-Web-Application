package com.bank.unitedbank.controller;

import com.bank.unitedbank.dto.request.*;
import com.bank.unitedbank.exception.OtpException;
import com.bank.unitedbank.exception.PasswordIncorrectException;
import com.bank.unitedbank.exception.UnauthorizedAccountException;
import com.bank.unitedbank.service.CustomerService;
import com.bank.unitedbank.service.EmailService;
import com.bank.unitedbank.service.OtpService;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
public class ProfileController {

	private final CustomerService customerService;
	private final EmailService emailService;
	private final OtpService otpService;

	public ProfileController(
			CustomerService cService,
			EmailService eService,
			OtpService otpService
	) {
		
		this.customerService = cService;
		this.emailService = eService;
		this.otpService = otpService;
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

	//just to verify the otp sent
	@PostMapping("/forgot/email")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {

		emailService.passResetEmail(forgotPasswordDTO.getEmail());

		return new ResponseEntity<>( "OTP sent successfully!", HttpStatus.OK);
	}

	@PostMapping("/forgot/reset")
	public ResponseEntity<?> passResetOtpValidation(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {

		if(forgotPasswordDTO.getOtp() == null){
			throw new OtpException("Fill the OTP!");
		}
		if(forgotPasswordDTO.getNewPassword() == null){
			throw new PasswordIncorrectException("Enter new Password");
		}

		boolean isOtpValid =
				otpService.isOtpValid(
						forgotPasswordDTO.getEmail(),
						forgotPasswordDTO.getOtp()
				);

		if(!isOtpValid){
			throw new OtpException("Wrong OTP!");
		}

		customerService.passwordUpdate(
				forgotPasswordDTO.getEmail(),
				forgotPasswordDTO.getNewPassword()
		);

		//delete otp after its use is over
		otpService.deleteOtp(forgotPasswordDTO.getEmail());

		return new ResponseEntity<>( "Password changed successfully!", HttpStatus.OK);
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