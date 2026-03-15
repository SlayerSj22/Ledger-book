package com.shashwat.ledger.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddPaymentRequest {

    private Long accountId;

    private Double amount;

    /**
     * CREDIT or DEBIT
     */
    private String type;

    private String description;
}
