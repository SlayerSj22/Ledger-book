package com.shashwat.ledger.controller;

import com.shashwat.ledger.dto.ApiResponse;
import com.shashwat.ledger.dto.PartyRegistrationRequest;
import com.shashwat.ledger.dto.PartyResponse;
import com.shashwat.ledger.service.PartyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping
    public ApiResponse<PartyResponse> createParty(
            @RequestBody PartyRegistrationRequest request) {

        PartyResponse response = partyService.createParty(request);

        return ApiResponse.<PartyResponse>builder()
                .data(response)
                .message("Party created successfully")
                .status(201)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<PartyResponse>> searchParty(
            @RequestParam String query) {

        List<PartyResponse> parties = partyService.searchParty(query);

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

    @GetMapping
    public ApiResponse<List<PartyResponse>> getAllParties() {

        List<PartyResponse> parties = partyService.getAllParties();

        return ApiResponse.<List<PartyResponse>>builder()
                .data(parties)
                .message("All parties fetched successfully")
                .status(200)
                .build();
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