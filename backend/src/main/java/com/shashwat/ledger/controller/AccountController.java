package com.shashwat.ledger.controller;

import com.shashwat.ledger.dto.AccountCreateRequest;
import com.shashwat.ledger.dto.AccountResponse;
import com.shashwat.ledger.dto.ApiResponse;
import com.shashwat.ledger.model.Account;
import com.shashwat.ledger.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create a new bill (Account) for a customer
     */
    @PostMapping("/create")
    public ApiResponse<Account> createAccount(
            @RequestBody AccountCreateRequest request) {

        Account account = accountService.createAccount(request);

        return ApiResponse.<Account>builder()
                .data(account)
                .message("Bill created successfully")
                .status(201)
                .build();
    }




    @GetMapping("/pending")
    public ApiResponse<Page<AccountResponse>> getPendingAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        System.out.println("before fetching");

        Page<AccountResponse> response =
                accountService.getTopPendingAccounts(page, size);

        System.out.println("after fetching");

        return ApiResponse.<Page<AccountResponse>>builder()
                .data(response)
                .message("Pending accounts fetched successfully")
                .status(200)
                .build();
    }

    @GetMapping("/party/{partyId}")
    public ApiResponse<List<AccountResponse>> getAccountsByParty(
            @PathVariable Long partyId) {

        System.out.println("Inside controller");

        List<AccountResponse> responses =
                accountService.getAccountsForParty(partyId);

        System.out.println("After fetching");

        return ApiResponse.<List<AccountResponse>>builder()
                .data(responses)
                .message("Accounts fetched successfully")
                .status(200)
                .build();
    }


    @DeleteMapping("/{accountId}")
    public ApiResponse<Void> deleteAccount(@PathVariable Long accountId) {

        accountService.deleteAccount(accountId);

        return ApiResponse.<Void>builder()
                .data(null)
                .message("Account deleted successfully")
                .status(200)
                .build();
    }
    @GetMapping("/{accountId}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable Long accountId) {

        AccountResponse response = accountService.getAccountById(accountId);

        return ApiResponse.<AccountResponse>builder()
                .data(response)
                .message("Account fetched successfully")
                .status(200)
                .build();
    }

    @GetMapping("/{accountId}/status")
    public ApiResponse<String> getAccountStatus(@PathVariable Long accountId) {

        String status = accountService.getAccountStatus(accountId);

        return ApiResponse.<String>builder()
                .data(status)
                .message("Account status fetched successfully")
                .status(200)
                .build();
    }




}
