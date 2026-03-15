package com.shashwat.ledger.controller;

import com.shashwat.ledger.dto.AddPaymentRequest;
import com.shashwat.ledger.dto.ApiResponse;
import com.shashwat.ledger.dto.LedgerEntryResponse;
import com.shashwat.ledger.dto.UpdateLedgerEntryRequest;
import com.shashwat.ledger.model.LedgerEntry;
import com.shashwat.ledger.repository.LedgerEntryRepository;
import com.shashwat.ledger.service.LedgerService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * Add a payment / adjustment to an account
     */
    @PostMapping("/add")
    public ApiResponse<LedgerEntryResponse> addPayment(
            @RequestBody AddPaymentRequest request) {



        LedgerEntry entry = ledgerService.addPayment(request);
        LedgerEntryResponse response = ledgerService.mapToLedgerResponse(entry);

        return ApiResponse.<LedgerEntryResponse>builder()
                .data(response)
                .message("Ledger entry added successfully")
                .status(201)
                .build();
    }


    /**
     * Fetch ledger history for a bill (account)
     */
    @GetMapping("/account/{accountId}")
    public ApiResponse<List<LedgerEntryResponse>> getLedgerForAccount(
            @PathVariable Long accountId) {

        List<LedgerEntryResponse> responses = new ArrayList<>();
        List<LedgerEntry> entries=ledgerService.getLedgerForAccount(accountId);

        for(LedgerEntry entry:entries){
            LedgerEntryResponse response=ledgerService.mapToLedgerResponse(entry);
            responses.add(response);
        }

        return ApiResponse.<List<LedgerEntryResponse>>builder()
                .data(responses)
                .message("Ledger entries fetched successfully")
                .status(200)
                .build();
    }

    @DeleteMapping("/entry/{entryId}")
    public ApiResponse<Void> deleteLedgerEntry(
            @PathVariable Long entryId) {

        ledgerService.deleteLedgerEntry(entryId);

        return ApiResponse.<Void>builder()
                .data(null)
                .message("Ledger entry deleted successfully")
                .status(200)
                .build();
    }

    @PutMapping("/entry/{entryId}")
    public ApiResponse<LedgerEntryResponse> updateLedgerEntry(
            @PathVariable Long entryId,
            @RequestBody UpdateLedgerEntryRequest request) {

        LedgerEntry entry =
                ledgerService.updateLedgerEntry(entryId, request);

        return ApiResponse.<LedgerEntryResponse>builder()
                .data(ledgerService.mapToLedgerResponse(entry))
                .message("Ledger entry updated successfully")
                .status(200)
                .build();
    }

    @GetMapping("/entry/{entryId}")
    public ApiResponse<LedgerEntryResponse> getLedgerEntry(
            @PathVariable Long entryId) {

        LedgerEntry entry = ledgerService.getEntry(entryId);

        return ApiResponse.<LedgerEntryResponse>builder()
                .data(ledgerService.mapToLedgerResponse(entry))
                .message("Ledger entry fetched")
                .status(200)
                .build();
    }


}

