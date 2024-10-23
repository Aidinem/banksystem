package com.azki.banksystem.service.impl;

import com.azki.banksystem.dao.BankAccountRepository;
import com.azki.banksystem.log.TransactionLogger;
import com.azki.banksystem.model.BankAccount;
import com.azki.banksystem.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    TransactionLogger transactionLogger;


    @Override
    @Transactional
    public synchronized BankAccount createAccount(String accountHolderName, double initialBalance) throws Exception {
        BankAccount account = new BankAccount();
        account.setAccountHolderName(accountHolderName);
        account.setBalance(initialBalance);
        return bankAccountRepository.save(account);
    }


    @Transactional
    public void deposit(String accountNumber, double amount) {
        // ... (deposit logic)
        transactionLogger.onTransaction(accountNumber, "Deposit", amount);
    }
}
