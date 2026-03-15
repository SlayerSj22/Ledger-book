package com.shashwat.ledger.repository;

import com.shashwat.ledger.dto.AccountResponse;
import com.shashwat.ledger.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
SELECT a
FROM Account a
JOIN FETCH a.party
WHERE a.status = :status
ORDER BY a.pendingAmount DESC
""")
    Page<Account> findOpenAccounts(@Param("status") String status, Pageable pageable);

    @Query("""
    SELECT a FROM Account a
    JOIN FETCH a.party
    WHERE a.party.id = :partyId
    """)
    List<Account> findByPartyId(Long partyId);
    @Query("SELECT a.status FROM Account a WHERE a.id = :id")
    String findStatusById(@Param("id") Long id);

    boolean existsByPartyId(Long partyId);

}