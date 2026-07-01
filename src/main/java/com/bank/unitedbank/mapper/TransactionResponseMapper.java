package com.bank.unitedbank.mapper;

import com.bank.unitedbank.dto.response.TransactionDataDTO;
import com.bank.unitedbank.entity.Transaction;

import java.util.List;

public class TransactionResponseMapper {

    public static TransactionDataDTO toTransactionResponseDTO(Transaction transaction){

        return new TransactionDataDTO(
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimeStamp()
        );
    }

    //for more than one transaction sending
    public static List<TransactionDataDTO> toListOfTransactionResponseDTO(List<Transaction> transactions){

        return transactions.stream()
                .map(TransactionResponseMapper::toTransactionResponseDTO)
                .toList();
    }

}
