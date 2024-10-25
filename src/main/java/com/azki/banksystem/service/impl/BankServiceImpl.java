package com.azki.banksystem.service.impl;

import com.azki.banksystem.dao.BankAccountRepository;
import com.azki.banksystem.log.TransactionLogger;
import com.azki.banksystem.model.BankAccount;
import com.azki.banksystem.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerException;
import java.util.Random;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    TransactionLogger transactionLogger;


    @Override
    @Transactional
    public synchronized BankAccount createAccount(BankAccount bankAccount) throws Exception {
        BankAccount account = new BankAccount();
        account.setAccountHolderName(bankAccount.getAccountHolderName());
        account.setAccountNumber(generateAccountNumber());
        if(bankAccount.getBalance() == 0 || bankAccount.getBalance() < 0)
            throw new ServerException("The initial balance value must be greater than zero.");
        account.setBalance(bankAccount.getBalance());
        return bankAccountRepository.save(account);
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) throws Exception {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account != null && account.getId() > 0)
            return account;
        else
            throw new Exception("Account Not Found With this accountNumber.");
    }

    @Override
    @Transactional
    public synchronized void deposit(String accountNumber, Double amount) throws Exception {
        if(accountNumber != null && !accountNumber.isEmpty()) {
            BankAccount account = findByAccountNumber(accountNumber);
            account.setBalance(account.getBalance() + amount);
            bankAccountRepository.save(account);
            transactionLogger.onTransaction(accountNumber, "Deposit", amount);
        } else
            throw new Exception("accountNumber is null");
    }

    @Override
    @Transactional
    public synchronized void withdraw(String accountNumber, Double amount) throws Exception {
        if(accountNumber != null && !accountNumber.isEmpty()) {
            BankAccount account = findByAccountNumber(accountNumber);
            if(account.getBalance() >= amount) {
                account.setBalance(account.getBalance() + amount);
                bankAccountRepository.save(account);
                transactionLogger.onTransaction(accountNumber, "Withdrawal", amount);
            }else
                throw new Exception("The Balance is less than amount");
        }else
            throw new Exception("accountNumber is null");
    }

    public String generateAccountNumber() {
        Random rand = new Random();
        StringBuilder accountNum = new StringBuilder("IR");
        for (int i = 0; i < 14; i++) {
            accountNum.append(rand.nextInt(10));
            if ((i + 1) % 4 == 0 && i != 13) {
                accountNum.append(" ");
            }
        }
        return accountNum.toString();
    }

}
