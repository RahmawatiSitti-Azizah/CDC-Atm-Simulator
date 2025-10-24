package com.mitrais.cdc.repository;

import com.mitrais.cdc.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    public List<Transaction> findBySourceAccountAccountNumber(String sourceAccount);

    public Transaction findByReferenceNumber(String referenceNumber);
}
