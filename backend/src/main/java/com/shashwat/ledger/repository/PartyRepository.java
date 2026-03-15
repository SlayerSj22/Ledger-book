package com.shashwat.ledger.repository;

import com.shashwat.ledger.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party,Long> {
    Optional<Party> findByNameAndFatherNameAndVillage(
            String name,
            String fatherName,
            String village
    );

    @Query("""
    SELECT p FROM Party p
    WHERE
    LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(p.fatherName) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(p.village) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Party> searchParty(@Param("query") String query);

}
