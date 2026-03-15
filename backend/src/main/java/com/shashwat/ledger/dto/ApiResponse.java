package com.shashwat.ledger.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private T data;
    private String message;
    private Integer status;
}

