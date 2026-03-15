package com.shashwat.ledger.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Each entry belongs to one Account
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Double amount;

    /**
     * CREDIT / DEBIT
     */
    @Column(nullable = false)
    private String type;

    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}