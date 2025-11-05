package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.mapper.AccountMapper;
import com.mitrais.cdc.repository.AccountRepository;
import com.mitrais.cdc.repository.TransactionRepository;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.util.ErrorConstant;
import com.mitrais.cdc.util.ReferenceNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Random;

public class AccountTransactionServiceImplTest {
    private static final double BALANCE_AMOUNT = 100.0;
    private AccountTransactionServiceImpl serviceInTest;
    private TransactionRepository transactionRepo = Mockito.mock(TransactionRepository.class);
    private AccountRepository accountRepo = Mockito.mock(AccountRepository.class);

    public static AccountTransactionService getAccountTransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        return new AccountTransactionServiceImpl(transactionRepository, accountRepository);
    }

    @BeforeEach
    public void setUp() throws Exception {
        serviceInTest = new AccountTransactionServiceImpl(transactionRepo, accountRepo);
    }

    @Test
    public void testWithdraw_transactionValid_thenBalanceDeducted() throws Exception {
        AccountDto account = new AccountDto("Jane", "112233", new Dollar(BALANCE_AMOUNT));
        Dollar transactionAmount = new Dollar(20.0);
        Mockito.when(accountRepo.findAccountByAccountNumber(Mockito.anyString())).thenReturn(AccountMapper.toEntity(account));
        Mockito.when(transactionRepo.save(Mockito.any(Transaction.class))).thenReturn(new Transaction(null, AccountMapper.toEntity(account), null, transactionAmount, "", "withdraw", LocalDateTime.now()));
        serviceInTest.withdraw(account, transactionAmount);
        Assertions.assertEquals(account.getBalance().toString(), "$80.0");
        Mockito.verify(transactionRepo, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testWithdraw_transactionAmountExceedAccountBalance_thenThrowException() throws Exception {
        AccountDto account = new AccountDto("Jane", "112233", new Dollar(BALANCE_AMOUNT));
        Mockito.when(accountRepo.findAccountByAccountNumber(Mockito.anyString())).thenReturn(AccountMapper.toEntity(account));
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.withdraw(account, new Dollar(110.0)));
        Assertions.assertEquals("Insufficient balance $110.0", exception.getMessage());
        Assertions.assertEquals(account.getBalance().toString(), "$100.0");
        Mockito.verify(transactionRepo, Mockito.never()).save(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testTransfer_transactionValid_thenSuccessDeductedSourceBalanceAndIncreaseDestBalance() throws Exception {
        AccountDto sourceAccount = new AccountDto("Jane", "112233", new Dollar(BALANCE_AMOUNT));
        AccountDto destinationAccount = new AccountDto("John", "223344", new Dollar(50.0));
        Mockito.when(accountRepo.findAccountByAccountNumber("112233")).thenReturn(AccountMapper.toEntity(sourceAccount));
        Mockito.when(accountRepo.findAccountByAccountNumber("223344")).thenReturn(AccountMapper.toEntity(destinationAccount));
        Dollar transactionAmount = new Dollar(20.0);
        Transaction transaction = new Transaction(null, AccountMapper.toEntity(sourceAccount), AccountMapper.toEntity(destinationAccount), transactionAmount, ReferenceNumberGenerator.generateWithdrawRefnumber(new Random()), "Transfer", LocalDateTime.now());
        Mockito.when(transactionRepo.save(Mockito.any(Transaction.class))).thenReturn(transaction);
        serviceInTest.transfer(sourceAccount, destinationAccount, transactionAmount, "REF123");
        Assertions.assertEquals(sourceAccount.getBalance().toString(), "$80.0");
        Assertions.assertEquals(destinationAccount.getBalance().toString(), "$70.0");
        Mockito.verify(transactionRepo, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(accountRepo, Mockito.times(2)).save(Mockito.any());
    }

    @Test
    public void testTransfer_transactionInvalid_thenThrowException() throws Exception {
        AccountDto sourceAccount = new AccountDto("Jane", "112233", new Dollar(BALANCE_AMOUNT));
        AccountDto destinationAccount = new AccountDto("John", "223344", new Dollar(50.0));
        Mockito.when(accountRepo.findAccountByAccountNumber("112233")).thenReturn(AccountMapper.toEntity(sourceAccount));
        Mockito.when(accountRepo.findAccountByAccountNumber("223344")).thenReturn(AccountMapper.toEntity(destinationAccount));
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.transfer(sourceAccount, destinationAccount, new Dollar(110.0), "REF123"));
        Assertions.assertEquals("Insufficient balance $110.0", exception.getMessage());
        Assertions.assertEquals(sourceAccount.getBalance().toString(), "$100.0");
        Assertions.assertEquals(destinationAccount.getBalance().toString(), "$50.0");
        Mockito.verify(transactionRepo, Mockito.never()).save(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testTransfer_whenSourceAndDestinationAccountSame_thenThrowDestinationAccountSameAsSourceException() throws Exception {
        AccountDto sourceAccount = new AccountDto("Jane", "112233", new Dollar(BALANCE_AMOUNT));
        Exception exception = Assertions.assertThrows(Exception.class, () -> serviceInTest.transfer(sourceAccount, sourceAccount, new Dollar(20.0), "REF123"));
        Assertions.assertEquals(ErrorConstant.DESTINATION_ACCOUNT_IS_THE_SAME_AS_SOURCE_ACCOUNT, exception.getMessage());
        Assertions.assertEquals((new Dollar(BALANCE_AMOUNT)).toString(), sourceAccount.getBalance().toString());
        Mockito.verify(transactionRepo, Mockito.never()).save(Mockito.any());
        Mockito.verify(accountRepo, Mockito.never()).save(Mockito.any());
    }
}