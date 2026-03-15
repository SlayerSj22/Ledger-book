package com.shashwat.ledger.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyRegistrationRequest {

    private String name;

    private String fatherName;

    private String village;

    private String phone;
}

