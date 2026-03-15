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

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PartyRepository partyRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public AccountService(AccountRepository accountRepository,
                          PartyRepository partyRepository,
                          LedgerEntryRepository ledgerEntryRepository) {
        this.accountRepository = accountRepository;
        this.partyRepository = partyRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    /**
     * Create a new account (bill)
     * Also creates the initial ledger entry
     */
    @Transactional
    public Account createAccount(AccountCreateRequest request) {

        Party party = partyRepository.findById(request.getPartyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + request.getPartyId()
                        ));

        LocalDateTime now = LocalDateTime.now();

        Account account = Account.builder()
                .party(party)
                .totalAmount(request.getTotalAmount())
                .totalCredit(0.0)
                .totalDebit(0.0) // ✅ FIX
                .pendingAmount(request.getTotalAmount())
                .status("OPEN")
                .description(request.getDescription())
                .createdDate(now)
                .build();

        accountRepository.save(account);

        // Initial ledger entry (correct)
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

    /**
     * Fetch OPEN accounts sorted by pending amount
     * Used for home page dashboard
     */
    public Page<AccountResponse> getTopPendingAccounts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Account> accounts =
                accountRepository.findOpenAccounts("OPEN", pageable);

        return accounts.map(this::mapToResponse);
    }

    /**
     * Fetch all accounts of a specific party
     */
    public List<AccountResponse> getAccountsForParty(Long partyId) {

        if (!partyRepository.existsById(partyId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with id: " + partyId
            );
        }


        List<Account> accounts = accountRepository.findByPartyId(partyId);

//        for(Account a:accounts){
//            System.out.println(a.getPendingAmount()+" "+a.getTotalAmount());
//        }

        return accounts.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        accountRepository.delete(account);
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

    @Transactional
    public AccountResponse getAccountById(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        return mapToResponse(account);
    }

    public String getAccountStatus(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        return account.getStatus();
    }
}