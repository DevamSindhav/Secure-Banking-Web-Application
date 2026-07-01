package com.bank.unitedbank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//configuration marks class as source of bean definitions 
@Configuration
public class ProjectConfig {

	//beans helps to inject the dependencies in the project 
	//it manages the object creation for us
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
		
	}
	
}
