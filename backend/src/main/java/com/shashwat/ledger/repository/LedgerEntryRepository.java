package com.shashwat.ledger.repository;

import com.shashwat.ledger.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    /**
     * Fetch all ledger entries for a given account (bill),
     * ordered by creation time (oldest first)
     */
    List<LedgerEntry> findByAccountIdOrderByCreatedDateAsc(Long accountId);
}

