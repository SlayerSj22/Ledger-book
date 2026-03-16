package com.shashwat.ledger.repository;

import com.shashwat.ledger.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    @Query("""
    SELECT l
    FROM LedgerEntry l
    JOIN FETCH l.account a
    JOIN FETCH a.party p
    WHERE a.id = :accountId
    AND p.user.email = :email
    ORDER BY l.createdDate ASC
    """)
    List<LedgerEntry> findByAccountIdAndUserEmail(
            @Param("accountId") Long accountId,
            @Param("email") String email
    );

    Optional<LedgerEntry> findByIdAndAccountPartyUserEmail(
            Long id,
            String email
    );
}