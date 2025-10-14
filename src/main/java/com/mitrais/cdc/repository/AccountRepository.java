package com.mitrais.cdc.repository;

import com.mitrais.cdc.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    public Account findAccountByAccountNumber(String accountNumber);
}
