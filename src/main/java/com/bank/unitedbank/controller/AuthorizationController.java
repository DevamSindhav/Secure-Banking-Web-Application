//this controller handles all the public action a user can take 
//like login, logout , or register etc.

package com.bank.unitedbank.controller;

import com.bank.unitedbank.entity.Account;
import com.bank.unitedbank.entity.Customer;
import com.bank.unitedbank.service.CustomerService;
import com.bank.unitedbank.dto.request.RegisterDTO;
import com.bank.unitedbank.dto.request.LoginDTO;
import com.bank.unitedbank.dto.response.AuthResponse;
import com.bank.unitedbank.mapper.CreateRequestMapper;
import com.bank.unitedbank.security.JwtUtil;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthorizationController{
	
	private final CustomerService customerService;
	private final JwtUtil jwtUtil;
	
	public AuthorizationController(CustomerService customerService, JwtUtil jwtUtil) {
		
		this.customerService = customerService;
		this.jwtUtil = jwtUtil;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registrationProcess(@Valid @RequestBody RegisterDTO registerDTO) {

		//Convert the DTO fields in to a real Customer object

		Customer customer = CreateRequestMapper.toCustomerEntity(registerDTO);
		Account account   = CreateRequestMapper.toAccountEntity(registerDTO);

		//success
		customerService.registerCustomer(customer , account);

		return new ResponseEntity<>(new AuthResponse("Customer registered successfully!") , HttpStatus.CREATED);
		
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginProcess(@Valid @RequestBody LoginDTO loginDTO) {
			//verification of user
			Customer validCustomer =
					customerService.loginValidation(loginDTO.getEmail() , loginDTO.getPassword());

			String token = jwtUtil.generateToken(validCustomer.getCustomerId());

			AuthResponse authResponse = new AuthResponse("Logged in successfully!" , token);

			return new ResponseEntity<>( authResponse , HttpStatus.OK);

	}
	
}
