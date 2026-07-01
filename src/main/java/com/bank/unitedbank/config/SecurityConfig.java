package com.bank.unitedbank.config;

import com.bank.unitedbank.dto.response.ErrorResponse;
import com.bank.unitedbank.security.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF so your custom POST forms (like login/deposit) work
            .csrf(csrf -> csrf.disable())

            //make spring completely stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            //allow for a specific requests to pass through without tokens like for login and registering
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**" , "/verify/**" , "/update/forgot/**").permitAll()//login and register public
                    .anyRequest().authenticated()
            )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request , response , authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");

                            ErrorResponse errorResponse = new ErrorResponse(
                                    401,
                                    "UNAUTHORIZED",
                                    "Authentication token is missing. Please log in.",
                                    Instant.now()
                            );

                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                            objectMapper.writeValue(response.getWriter() , errorResponse);

                        })
                )
                .addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}