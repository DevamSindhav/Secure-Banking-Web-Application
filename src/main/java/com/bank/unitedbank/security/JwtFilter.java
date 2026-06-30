package com.bank.unitedbank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import com.bank.unitedbank.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )throws ServletException, IOException {
        //find authorization header
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        Long customerId = null;

        //see if it contains Bearer keyword as all JWT
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);

            try{
                customerId = jwtUtil.extractCustomerId(jwt);

            }catch(Exception e){
                //If exception occurred handle it by mapping it as JSON

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");

                ErrorResponse errorResponse =
                        new ErrorResponse(
                                401,
                                "UNAUTHORIZED",
                                "Token is expired or invalid. Please log in again.",
                                Instant.now()
                        );
                ObjectMapper objectMapper = new ObjectMapper();

                objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                objectMapper.writeValue(response.getWriter() , errorResponse);

                return;
            }

        }

        if(customerId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //create authentication object
            //pass customerId as the identity
            //pass Empty arrayList for indicating user is verified
            if(jwtUtil.isTokenValid(jwt)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(customerId , null , new ArrayList<>());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(request , response);
    }

}
