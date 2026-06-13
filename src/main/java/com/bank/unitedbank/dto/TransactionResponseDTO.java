package com.bank.unitedbank.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.bank.unitedbank.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private Integer transactionId;
    private BigDecimal amount;
    private String transactionType;
    private LocalDate date;
    private LocalTime time;

    //To convert a Transaction to the TransactionResponseDTO type
    public static TransactionResponseDTO convertTransactionToTRDTO(Transaction transaction){
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getTransactionType(),

                //separated the local time and date
                transaction.getTimeStamp().toLocalDate(),
                transaction.getTimeStamp().toLocalTime().withNano(0)
                //with nano removes milliseconds from transaction time
        );
    }

}