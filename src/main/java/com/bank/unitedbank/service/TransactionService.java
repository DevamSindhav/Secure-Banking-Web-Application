package com.bank.unitedbank.service;

import com.bank.unitedbank.entity.Account;
import jakarta.transaction.Transactional;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.bank.unitedbank.entity.Transaction;
import com.bank.unitedbank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository tRepository) {
		this.transactionRepository = tRepository;
	}

	public void recordTransaction(Account account , BigDecimal amount , String transactionType){

		Transaction newTransaction = new Transaction(account , amount , transactionType);

		transactionRepository.save(newTransaction);
	}
	
	public List<Transaction> getAllStatement(Long accNo){
		
		return transactionRepository.findByAccountAccNoOrderByTimeStampDesc(accNo);
	}
	
	public List<Transaction> getMiniStatement(Long accNo){
		
		return transactionRepository.findTop10ByAccountAccNoOrderByTimeStampDesc(accNo);
	}

	public List<Transaction> getMonthStatement(Long accNo, Instant startDate , Instant endDate ){

		return transactionRepository.findByAccountAccNoAndTimeStampBetweenOrderByTimeStampDesc(accNo , startDate , endDate);
	}
	
}
