package com.shashwat.ledger.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntryResponse {

    private Long id;
    private Long accountId;
    private Double amount;
    private String type;
    private String description;
    private LocalDateTime createdDate;
}
