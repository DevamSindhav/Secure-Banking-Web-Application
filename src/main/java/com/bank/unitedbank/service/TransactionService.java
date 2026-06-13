/*
code can be shorter if I don't initialize temporary variables,
but I have decided to keep those vars for the readability
*/

/*
* I came to know that accessing a customerRepo might be trespassing in the customers domain
* I can make a Customer Service method to do this but that will create cycle between two service
* decided to keep it as it is
* if required can solve this by a new Service called as AccountService
* */

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
	
	
//	public BigDecimal deposit(Account account , BigDecimal amount) {
//
//		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//	        throw new RuntimeException("Deposit amount must be greater than zero.");
//	    }
//
//		Transaction transaction = new Transaction(account , amount , "DEPOSIT_00");
//
//		transactionRepository.save(transaction);
//
//		return account.getBalance().add(amount);
//	}
	
	
//	public BigDecimal withdraw(Account account , String plainPin , BigDecimal amount) {
//
//		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//	        throw new RuntimeException("Withdrawal amount must be greater than zero.");
//	    }
//
//		boolean isMatch = passwordEncoder.matches(plainPin , account.getPinHash());
//
//		if(!isMatch) {
//			throw new RuntimeException("Pin is incorrect.");
//		}
//
//		BigDecimal currentBalance = account.getBalance();
//
//		if(currentBalance.compareTo(amount) < 0) {
//
//			throw new RuntimeException("Not enough balance.");
//		}
//
//		BigDecimal newBalance = currentBalance.subtract(amount);
//
//		Transaction transaction = new Transaction(account , amount , "WITHDRAW_11");
//
//		transactionRepository.save(transaction);
//
//		return newBalance;
//	}
//
//	public void transfer(Account senderAcc ,String plainPin , Account receiverAcc , BigDecimal amount) {
//
//		if (senderAcc.getAccNo().equals(receiverAcc.getAccNo())) {
//	        throw new RuntimeException("Cannot transfer money to the same account.");
//	    }
//
//		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//	        throw new RuntimeException("Transfer amount must be greater than zero.");
//	    }
//
//		boolean isMatch = passwordEncoder.matches(plainPin , senderAcc.getPinHash() );
//
//		if(!isMatch) {
//			throw new RuntimeException("Pin is incorrect.");
//		}
//
//		BigDecimal senderCurrentBalance = senderAcc.getBalance();
//		BigDecimal receiverCurrentBalance = receiverAcc.getBalance();
//
//		if(senderCurrentBalance.compareTo(amount) < 0) {
//			throw new RuntimeException("Not enough balance.");
//		}
//
//		BigDecimal senderNewBalance = senderCurrentBalance.subtract(amount);
//		BigDecimal receiverNewBalance = receiverCurrentBalance.add(amount);
//
//		senderAcc.setBalance(senderNewBalance);
//		receiver.setBalance(receiverNewBalance);
//
//		Transaction senderTransaction = new Transaction(senderAcc , amount , "TRANSFER_TO_" + receiverAcc.getAccNo().toString());
//		Transaction receiverTransaction = new Transaction(receiverAcc , amount , "TRANSFER_FROM_" + senderAcc.getAccNo().toString() );
//
//		transactionRepository.save(senderTransaction);
//		transactionRepository.save(receiverTransaction);
//
//		return ??
//
//	}

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
