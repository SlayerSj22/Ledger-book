package com.shashwat.ledger.controller;

import com.shashwat.ledger.dto.ApiResponse;
import com.shashwat.ledger.dto.PartyRegistrationRequest;
import com.shashwat.ledger.dto.PartyResponse;
import com.shashwat.ledger.model.Party;
import com.shashwat.ledger.service.PartyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping("/register")
    public ApiResponse<Party> registerUser(
            @RequestBody PartyRegistrationRequest request) {

        Party savedParty = partyService.createParty(request);

        return ApiResponse.<Party>builder()
                .data(savedParty)
                .message("Customer registered successfully")
                .status(201)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<PartyResponse>> searchParty(
            @RequestParam String query
    ) {

        List<PartyResponse> parties =
                partyService.searchParty(query);

        return ApiResponse.<List<PartyResponse>>builder()
                .data(parties)
                .message("Search results fetched")
                .status(200)
                .build();
    }

    @GetMapping("/{partyId}")
    public ApiResponse<PartyResponse> getPartyById(
            @PathVariable Long partyId) {

        PartyResponse response = partyService.getPartyById(partyId);

        return ApiResponse.<PartyResponse>builder()
                .data(response)
                .message("Party fetched successfully")
                .status(200)
                .build();
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Party>>> getAllParty() {

        List<Party> parties = partyService.getAllParty();

        ApiResponse<List<Party>> response =
                ApiResponse.<List<Party>>builder()
                        .data(parties)
                        .message("Parties fetched successfully")
                        .status(200)
                        .build();

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{partyId}")
    public ApiResponse<Void> deleteParty(@PathVariable Long partyId) {

        partyService.deleteParty(partyId);

        return ApiResponse.<Void>builder()
                .data(null)
                .message("Party deleted successfully")
                .status(200)
                .build();
    }
}

