package com.shashwat.ledger.service;

import com.shashwat.ledger.dto.PartyRegistrationRequest;
import com.shashwat.ledger.dto.PartyResponse;
import com.shashwat.ledger.exception.DuplicatePartyException;
import com.shashwat.ledger.exception.ResourceNotFoundException;
import com.shashwat.ledger.model.Party;
import com.shashwat.ledger.model.User;
import com.shashwat.ledger.repository.AccountRepository;
import com.shashwat.ledger.repository.PartyRepository;
import com.shashwat.ledger.repository.UserRepository;
import com.shashwat.ledger.security.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    public PartyService(
            PartyRepository partyRepository,
            AccountRepository accountRepository,
            UserRepository userRepository,
            SecurityUtil securityUtil) {

        this.partyRepository = partyRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }

    public PartyResponse createParty(PartyRegistrationRequest request) {

        String name = request.getName().trim().toLowerCase();
        String fatherName = request.getFatherName().trim().toLowerCase();
        String village = request.getVillage().trim().toLowerCase();

        boolean exists = partyRepository
                .findByNameAndFatherNameAndVillageAndUserEmail(
                        name, fatherName, village, securityUtil.getCurrentUserEmail())
                .isPresent();

        if (exists) {
            throw new DuplicatePartyException(
                    "Customer already exists with same name, father name and village"
            );
        }

        String email = securityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Party party = Party.builder()
                .name(name)
                .fatherName(fatherName)
                .village(village)
                .phone(request.getPhone())
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();

        Party savedParty = partyRepository.save(party);

        return mapToResponse(savedParty);
    }

    public List<PartyResponse> searchParty(String query) {

        String email = securityUtil.getCurrentUserEmail();

        List<Party> parties =
                partyRepository.searchParty(query, email);

        return parties.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PartyResponse getPartyById(Long partyId) {

        String email = securityUtil.getCurrentUserEmail();

        Party party = partyRepository
                .findByIdAndUserEmail(partyId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Party not found with id: " + partyId
                        ));

        return mapToResponse(party);
    }

    public List<PartyResponse> getAllParties() {

        String email = securityUtil.getCurrentUserEmail();

        return partyRepository.findAllByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteParty(Long partyId) {

        String email = securityUtil.getCurrentUserEmail();

        Party party = partyRepository
                .findByIdAndUserEmail(partyId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Party not found with id: " + partyId
                        ));

        boolean hasAccounts =
                accountRepository.existsByPartyId(partyId);

        if (hasAccounts) {
            throw new IllegalStateException(
                    "Cannot delete party because it has accounts"
            );
        }

        partyRepository.delete(party);
    }

    private PartyResponse mapToResponse(Party party) {

        return PartyResponse.builder()
                .id(party.getId())
                .name(party.getName())
                .fatherName(party.getFatherName())
                .village(party.getVillage())
                .phone(party.getPhone())
                .build();
    }
}