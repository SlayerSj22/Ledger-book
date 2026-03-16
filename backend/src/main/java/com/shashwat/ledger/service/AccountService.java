package com.shashwat.ledger.service;

import com.shashwat.ledger.dto.AccountCreateRequest;
import com.shashwat.ledger.dto.AccountResponse;
import com.shashwat.ledger.exception.ResourceNotFoundException;
import com.shashwat.ledger.model.Account;
import com.shashwat.ledger.model.LedgerEntry;
import com.shashwat.ledger.model.Party;
import com.shashwat.ledger.repository.AccountRepository;
import com.shashwat.ledger.repository.LedgerEntryRepository;
import com.shashwat.ledger.repository.PartyRepository;
import com.shashwat.ledger.security.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PartyRepository partyRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final SecurityUtil securityUtil;

    public AccountService(AccountRepository accountRepository,
                          PartyRepository partyRepository,
                          LedgerEntryRepository ledgerEntryRepository,
                          SecurityUtil securityUtil) {

        this.accountRepository = accountRepository;
        this.partyRepository = partyRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.securityUtil = securityUtil;
    }

    @Transactional
    public Account createAccount(AccountCreateRequest request) {

        String email = securityUtil.getCurrentUserEmail();

        Party party = partyRepository
                .findByIdAndUserEmail(request.getPartyId(), email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + request.getPartyId()
                        ));

        LocalDateTime now = LocalDateTime.now();

        Account account = Account.builder()
                .party(party)
                .totalAmount(request.getTotalAmount())
                .totalCredit(0.0)
                .totalDebit(0.0)
                .pendingAmount(request.getTotalAmount())
                .status("OPEN")
                .description(request.getDescription())
                .createdDate(now)
                .build();

        accountRepository.save(account);

        LedgerEntry entry = LedgerEntry.builder()
                .account(account)
                .amount(request.getTotalAmount())
                .type("DEBIT")
                .description("Initial bill: " + request.getDescription())
                .createdDate(now)
                .build();

        ledgerEntryRepository.save(entry);

        return account;
    }

    @Transactional
    public Page<AccountResponse> getTopPendingAccounts(int page, int size) {

        String email = securityUtil.getCurrentUserEmail();
        System.out.println(email+" "+"!!!!!!!!!!!!!!!!!!!1");

        Pageable pageable = PageRequest.of(page, size);

        Page<Account> accounts =
                accountRepository.findOpenAccounts("OPEN", email, pageable);

        return accounts.map(this::mapToResponse);
    }

    @Transactional
    public List<AccountResponse> getAccountsForParty(Long partyId) {

        String email = securityUtil.getCurrentUserEmail();
        System.out.println(email+"   reached insdie service layer");

        List<Account> accounts =
                accountRepository.findByPartyIdAndUserEmail(partyId, email);
        System.out.println();

        return accounts.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public AccountResponse getAccountById(Long accountId) {

        String email = securityUtil.getCurrentUserEmail();

        Account account = accountRepository
                .findByIdAndPartyUserEmail(accountId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        System.out.println("fetched");

        return mapToResponse(account);
    }

    @Transactional
    public void deleteAccount(Long accountId) {

        String email = securityUtil.getCurrentUserEmail();

        Account account = accountRepository
                .findByIdAndPartyUserEmail(accountId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        accountRepository.delete(account);
    }

    public String getAccountStatus(Long accountId) {

        String email = securityUtil.getCurrentUserEmail();

        Account account = accountRepository
                .findByIdAndPartyUserEmail(accountId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        return account.getStatus();
    }

    private AccountResponse mapToResponse(Account account) {

        double totalAmount = account.getTotalAmount() == null ? 0 : account.getTotalAmount();
        double totalDebit = account.getTotalDebit() == null ? 0 : account.getTotalDebit();
        double totalCredit = account.getTotalCredit() == null ? 0 : account.getTotalCredit();

        double totalBill = totalAmount + totalDebit;
        double pending = totalBill - totalCredit;

        return AccountResponse.builder()
                .id(account.getId())
                .partyName(account.getParty().getName())
                .description(account.getDescription())
                .totalBill(totalBill)
                .pendingAmount(Math.max(pending, 0))
                .status(account.getStatus())
                .createdDate(account.getCreatedDate())
                .build();
    }
}