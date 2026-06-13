package com.bank.unitedbank.repository;

import com.bank.unitedbank.entity.Transaction;

import java.time.Instant;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction , Long>{

	List<Transaction> findByAccountAccNoOrderByTimeStampDesc(Long accNo);
	// Spring automatically translates this into a SQL query with "LIMIT 10"
	List<Transaction> findTop10ByAccountAccNoOrderByTimeStampDesc(Long accNo);

	//For the transactions between months
	//because sending all transaction can be performance heavy
	List<Transaction> findByAccountAccNoAndTimeStampBetweenOrderByTimeStampDesc(
			Long accNo ,
			Instant startDate,
			Instant endDate
	);
	
}
