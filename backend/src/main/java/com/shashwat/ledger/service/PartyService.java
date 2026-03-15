package com.shashwat.ledger.service;

import com.shashwat.ledger.dto.PartyRegistrationRequest;
import com.shashwat.ledger.dto.PartyResponse;
import com.shashwat.ledger.exception.DuplicatePartyException;
import com.shashwat.ledger.exception.ResourceNotFoundException;
import com.shashwat.ledger.model.Party;
import com.shashwat.ledger.repository.AccountRepository;
import com.shashwat.ledger.repository.PartyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final AccountRepository accountRepository;
    public PartyService(PartyRepository partyRepository,AccountRepository accountRepository){

        this.partyRepository=partyRepository;
        this.accountRepository=accountRepository;
    }

    public Party createParty(PartyRegistrationRequest request) {


        String name = request.getName().trim().toLowerCase();
        String fatherName = request.getFatherName().trim().toLowerCase();
        String village = request.getVillage().trim().toLowerCase();

//        System.out.println(name+" "+fatherName+" "+village);

        System.out.println("Before repository call");

        boolean exists = partyRepository
                .findByNameAndFatherNameAndVillage(name, fatherName, village)
                .isPresent();

        System.out.println("After repository call: " + exists);



        if (exists) {
            throw new DuplicatePartyException(
                    "Customer already exists with same name, father name and village"
            );
        }

        Party party = Party.builder()
                .name(name)
                .fatherName(fatherName)
                .village(village)
                .phone(request.getPhone())
                .createdDate(LocalDateTime.now())
                .build();

        return partyRepository.save(party);
    }

    public List<PartyResponse> searchParty(String query) {

        List<Party> parties = partyRepository.searchParty(query);

        return parties.stream()
                .map(this::mapToResponse)
                .toList();
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

    public PartyResponse getPartyById(Long partyId) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Party not found with id: " + partyId
                        ));

        return mapToResponse(party);
    }


    public List<Party> getAllParty() {
        List<Party> parties=partyRepository.findAll();
        return parties;
    }

    @Transactional
    public void deleteParty(Long partyId) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Party not found with id: " + partyId
                        ));

        boolean hasAccounts = accountRepository.existsByPartyId(partyId);

        if (hasAccounts) {
            throw new IllegalStateException(
                    "Cannot delete party because it has accounts"
            );
        }

        partyRepository.delete(party);
    }
}
