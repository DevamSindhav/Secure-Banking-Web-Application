package com.bank.unitedbank.repository;

import com.bank.unitedbank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account , Long> {

    //Does not need to check if isAccountValid true or not
    // the Reason is the SQLRestriction annotation
    //restricts teh sql query only where isAccountValid is true
    List<Account> findByCustomerCustomerId(Long customerId);

    boolean existsByCustomerCustomerId(Long customerId);

}
