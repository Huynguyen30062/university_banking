package com.universitybank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One-to-many relationship with transactions
    @OneToMany(mappedBy = "fromAccountObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "toAccountObj", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> incomingTransactions;
}
