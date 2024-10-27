package com.azki.banksystem.service.impl;

import com.azki.banksystem.dao.BankAccountRepository;
import com.azki.banksystem.log.TransactionObserver;
import com.azki.banksystem.model.BankAccount;
import com.azki.banksystem.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    TransactionObserver transactionObserver;


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
    public synchronized BankAccount createAccount(BankAccount bankAccount) throws Exception {
        BankAccount account = new BankAccount();
        account.setAccountHolderName(bankAccount.getAccountHolderName());
        account.setAccountNumber(generateAccountNumber());
        if(bankAccount.getBalance() == 0 || bankAccount.getBalance() < 0)
            throw new Exception("The initial balance value must be greater than zero.");
        account.setBalance(bankAccount.getBalance());
        return bankAccountRepository.save(account);
    }



    @Override
    @Transactional
    public synchronized void deposit(String accountNumber, Double amount) throws Exception {
        if(accountNumber != null && !accountNumber.isEmpty()) {
            BankAccount account = findByAccountNumber(accountNumber);
            account.setBalance(account.getBalance() + amount);
            bankAccountRepository.save(account);
            transactionObserver.notifyObservers(accountNumber,"Deposit", amount);
        } else
            throw new Exception("accountNumber is null");
    }

    @Override
    @Transactional
    public synchronized void withdraw(String accountNumber, Double amount) throws Exception {
        if(accountNumber != null && !accountNumber.isEmpty()) {
            BankAccount account = findByAccountNumber(accountNumber);
            if(checkBalanceValueWithAmount(account.getBalance(),amount)) {
                account.setBalance(account.getBalance() - amount);
                bankAccountRepository.save(account);
                transactionObserver.notifyObservers(accountNumber,"Withdrawal", amount);
            }else
                throw new Exception("The Balance is less than amount");
        }else
            throw new Exception("accountNumber is null");
    }

    @Override
    @Transactional
    public synchronized void transfer(String fromAccountNumber, String toAccountNumber, Double amount) throws Exception {
        if(fromAccountNumber != null && !fromAccountNumber.isEmpty() && toAccountNumber != null && !toAccountNumber.isEmpty()) {
            BankAccount originAccount = findByAccountNumber(fromAccountNumber);
            BankAccount destinationAccount = findByAccountNumber(toAccountNumber);
            withdraw(originAccount.getAccountNumber(),amount);
            deposit(destinationAccount.getAccountNumber(),amount);
        }else
            throw new Exception("fromAccount And toAccount cannot be null");
    }

    public Boolean checkBalanceValueWithAmount(Double balance,Double amount) {
        if(balance > amount)
            return Boolean.TRUE;
        return Boolean.FALSE;
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
