package com.shashwat.ledger.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many accounts belong to one party
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    /**
     * Original bill amount
     */
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    /**
     * Ledger summary
     */
    @Column(name = "total_credit", nullable = false)
    private Double totalCredit;

    @Column(name = "total_debit", nullable = false)
    private Double totalDebit;

    @Column(name = "pending_amount", nullable = false)
    private Double pendingAmount;

    /**
     * OPEN / CLOSED
     */
    @Column(nullable = false)
    private String status;

    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * Ledger entries for this account
     */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LedgerEntry> ledgerEntries;
}