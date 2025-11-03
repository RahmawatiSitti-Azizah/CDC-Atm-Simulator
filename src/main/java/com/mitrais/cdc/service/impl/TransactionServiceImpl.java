package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.model.mapper.TransactionMapper;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getTransactionHistoryAccount(String accountNumber) {
        return transactionRepository.findBySourceAccountAccountNumber(accountNumber);
    }

    @Override
    public List<TransactionDto> getTransactionHistoryAccount(String accountNumber, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "transactionDate");
        Pageable pageable = PageRequest.of(0, size, sort);
        Specification<Transaction> spec = Specification.anyOf(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("sourceAccount").get("accountNumber"), accountNumber),
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("destinationAccount").get("accountNumber"), accountNumber));
        Page<Transaction> page = transactionRepository.findAll(spec, pageable);
        List<Transaction> transactionList = page.getContent();
        return transactionList.size() != 0 ? transactionList.stream().map(transaction -> TransactionMapper.toTransactionDto(transaction)).toList() : List.of();
    }
}
