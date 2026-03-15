package com.shashwat.ledger.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;

    private String partyName;

    private String description;

    /**
     * baseAmount + totalDebit
     */
    private Double totalBill;

    private Double pendingAmount;

    private String status;

    private LocalDateTime createdDate;
}