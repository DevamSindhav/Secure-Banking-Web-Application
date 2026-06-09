package com.bank.Unitedbank.repository;

import com.bank.Unitedbank.entity.Transaction;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction , Integer>{
	
	List<Transaction> findByCustomerAccNoOrderByTimeStampDesc(Integer accNo);
	// Spring automatically translates this into a SQL query with "LIMIT 10"
	List<Transaction> findTop10ByCustomerAccNoOrderByTimeStampDesc(Integer accNo);

	//For the transactions between months
	//because sending all transaction can be performance heavy
	List<Transaction> findByCustomerAccNoAndTimeStampBetweenOrderByTimeStampDesc(
			Integer accNo ,
			LocalDateTime startDate,
			LocalDateTime endDate
	);
	
}
