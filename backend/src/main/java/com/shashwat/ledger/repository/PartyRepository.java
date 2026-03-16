package com.shashwat.ledger.repository;

import com.shashwat.ledger.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findByNameAndFatherNameAndVillageAndUserEmail(
            String name,
            String fatherName,
            String village,
            String email
    );

    @Query("""
    SELECT p FROM Party p
    WHERE p.user.email = :email
    AND (
        LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.fatherName) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.village) LIKE LOWER(CONCAT('%', :query, '%'))
    )
    """)
    List<Party> searchParty(
            @Param("query") String query,
            @Param("email") String email
    );

    List<Party> findAllByUserEmail(String email);

    Optional<Party> findByIdAndUserEmail(Long id, String email);
}