package com.universitybank.controllers;

import com.universitybank.entity.Account;
import com.universitybank.entity.User;
import com.universitybank.repository.UserRepository;
import com.universitybank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountsByUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Account> accounts = accountService.getAccountsByUser(user.get());
            return ResponseEntity.ok(accounts);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        String accountNumber = "ACCT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Account account = accountService.createAccount(user, accountNumber);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountNumber, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Optional<Account> account = accountService.getAccountByNumber(accountNumber);
        if (account.isPresent() && account.get().getUser().getId().equals(user.getId())) {
            accountService.deleteAccount(account.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).body("Forbidden");
    }
}
