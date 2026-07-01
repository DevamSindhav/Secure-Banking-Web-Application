package com.bank.unitedbank.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDataDTO {

    private Long customerId;
    private String fullName;
    private String email;
    private String address;
    private String mobileNo;
    private String postalCode;
    private LocalDate dob;
    private List<Long> accNoList;

}

