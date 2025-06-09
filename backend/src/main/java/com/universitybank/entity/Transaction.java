package com.universitybank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "from_account", referencedColumnName = "account_number", insertable = false, updatable = false)
    private Account fromAccountObj;

    @Column(name = "from_account", nullable = false)
    private String fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account", referencedColumnName = "account_number", insertable = false, updatable = false)
    private Account toAccountObj;

    @Column(name = "to_account", nullable = false)
    private String toAccount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
