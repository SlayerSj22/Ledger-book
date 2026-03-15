package com.shashwat.ledger.service;

import com.shashwat.ledger.dto.AddPaymentRequest;
import com.shashwat.ledger.dto.LedgerEntryResponse;
import com.shashwat.ledger.dto.UpdateLedgerEntryRequest;
import com.shashwat.ledger.exception.ResourceNotFoundException;
import com.shashwat.ledger.model.Account;
import com.shashwat.ledger.model.LedgerEntry;
import com.shashwat.ledger.repository.AccountRepository;
import com.shashwat.ledger.repository.LedgerEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;

    public LedgerService(LedgerEntryRepository ledgerEntryRepository,
                         AccountRepository accountRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Add a ledger entry (payment / adjustment) to an account
     */
    @Transactional
    public LedgerEntry addPayment(AddPaymentRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + request.getAccountId()));

        if ("CLOSED".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException("Account already closed");
        }

        double amount = request.getAmount();
        String type = request.getType().toUpperCase();

        // ✅ CREDIT validation
        if ("CREDIT".equals(type) && amount > account.getPendingAmount()) {
            throw new IllegalArgumentException(
                    "Payment cannot exceed pending amount (Pending: " + account.getPendingAmount() + ")"
            );
        }

        LedgerEntry entry = LedgerEntry.builder()
                .account(account)
                .amount(amount)
                .type(type)
                .description(request.getDescription())
                .createdDate(LocalDateTime.now())
                .build();

        ledgerEntryRepository.save(entry);

        if ("CREDIT".equals(type)) {
            account.setTotalCredit(account.getTotalCredit() + amount);
        } else if ("DEBIT".equals(type)) {
            account.setTotalDebit(account.getTotalDebit() + amount);
        }

        double totalBill = account.getTotalAmount() + account.getTotalDebit();
        double pending = totalBill - account.getTotalCredit();

        account.setPendingAmount(Math.max(pending, 0));

        if (pending <= 0) {
            account.setStatus("CLOSED");
        }

        return entry;
    }

    public List<LedgerEntry> getLedgerForAccount(Long accountId) {

        // Validate account exists
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException(
                    "Account not found with id: " + accountId
            );
        }

        return ledgerEntryRepository
                .findByAccountIdOrderByCreatedDateAsc(accountId);
    }

    public LedgerEntryResponse mapToLedgerResponse(LedgerEntry entry) {
        return LedgerEntryResponse.builder()
                .id(entry.getId())
                .accountId(entry.getAccount().getId())
                .amount(entry.getAmount())
                .type(entry.getType())
                .description(entry.getDescription())
                .createdDate(entry.getCreatedDate())
                .build();
    }

    @Transactional
    public void deleteLedgerEntry(Long entryId) {

        LedgerEntry entry = ledgerEntryRepository.findById(entryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ledger entry not found with id: " + entryId
                        ));

        Account account = entry.getAccount();

        if ("CREDIT".equalsIgnoreCase(entry.getType())) {
            account.setTotalCredit(account.getTotalCredit() - entry.getAmount());
        } else {
            account.setTotalDebit(account.getTotalDebit() - entry.getAmount());
        }

        ledgerEntryRepository.delete(entry);

        double pending =
                account.getTotalAmount()
                        + account.getTotalDebit()
                        - account.getTotalCredit();

        account.setPendingAmount(Math.max(pending, 0));

        if (pending > 0) {
            account.setStatus("OPEN");
        } else {
            account.setStatus("CLOSED");
        }
    }

    @Transactional
    public LedgerEntry updateLedgerEntry(
            Long entryId,
            UpdateLedgerEntryRequest request) {

        LedgerEntry entry = ledgerEntryRepository.findById(entryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ledger entry not found with id: " + entryId
                        ));

        Account account = entry.getAccount();

        // 1️⃣ revert old values
        if ("CREDIT".equalsIgnoreCase(entry.getType())) {
            account.setTotalCredit(account.getTotalCredit() - entry.getAmount());
        } else {
            account.setTotalDebit(account.getTotalDebit() - entry.getAmount());
        }

        // 2️⃣ update entry
        entry.setAmount(request.getAmount());
        entry.setDescription(request.getDescription());
        entry.setType(request.getType());

        ledgerEntryRepository.save(entry);

        // 3️⃣ apply new values
        if ("CREDIT".equalsIgnoreCase(request.getType())) {
            account.setTotalCredit(account.getTotalCredit() + request.getAmount());
        } else {
            account.setTotalDebit(account.getTotalDebit() + request.getAmount());
        }

        // 4️⃣ recalc pending
        double pending =
                account.getTotalAmount()
                        + account.getTotalDebit()
                        - account.getTotalCredit();

        account.setPendingAmount(Math.max(pending, 0));

        if (pending <= 0) {
            account.setStatus("CLOSED");
        } else {
            account.setStatus("OPEN");
        }

        return entry;
    }


    public LedgerEntry getEntry(Long entryId) {

        return ledgerEntryRepository.findById(entryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Entry not found"
                        ));
    }

}
