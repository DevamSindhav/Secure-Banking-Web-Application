package com.bank.unitedbank.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDataDTO {

    private Long transactionId;
    private BigDecimal amount;
    private String transactionType;
    private Instant timeStamp;

}