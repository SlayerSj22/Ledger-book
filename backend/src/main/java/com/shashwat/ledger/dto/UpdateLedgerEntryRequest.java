package com.shashwat.ledger.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLedgerEntryRequest {

    private Double amount;

    private String description;

    private String type; // CREDIT or DEBIT
}