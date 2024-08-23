package com.github.druzhininyury.testtask.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "operations")
public class Operation {

    public enum OperationType {

        DEPOSIT,
        WITHDRAW;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WalletBalance walletBalance;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

}
