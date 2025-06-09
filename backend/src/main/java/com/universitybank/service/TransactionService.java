package com.universitybank.service;

import com.universitybank.entity.Account;
import com.universitybank.entity.Transaction;
import com.universitybank.entity.User;
import com.universitybank.repository.AccountRepository;
import com.universitybank.repository.TransactionRepository;
import com.universitybank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String userEmail) throws Exception {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new Exception("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new Exception("Destination account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new Exception("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new Exception("User not found"));

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccountNumber);
        transaction.setToAccount(toAccountNumber);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }
}
