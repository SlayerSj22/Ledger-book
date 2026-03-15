package com.shashwat.ledger.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String email;
    private String password;
    private String role;

}