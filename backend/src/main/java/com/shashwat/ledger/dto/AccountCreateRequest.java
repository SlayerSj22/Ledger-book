package com.shashwat.ledger.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRequest {

    private Long partyId;

    private Double totalAmount;

    private String description;
}

