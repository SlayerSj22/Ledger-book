package com.shashwat.ledger.repository;

import com.shashwat.ledger.model.Account;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
    SELECT a
    FROM Account a
    JOIN a.party p
    WHERE a.status = :status
    AND p.user.email = :email
    ORDER BY a.pendingAmount DESC
    """)
    Page<Account> findOpenAccounts(
            @Param("status") String status,
            @Param("email") String email,
            Pageable pageable
    );

    @Query("""
    SELECT a
    FROM Account a
    JOIN a.party p
    WHERE p.id = :partyId
    AND p.user.email = :email
    """)
    List<Account> findByPartyIdAndUserEmail(
            @Param("partyId") Long partyId,
            @Param("email") String email
    );

    Optional<Account> findByIdAndPartyUserEmail(Long id, String email);

    boolean existsByPartyId(Long partyId);
}