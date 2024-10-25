package com.azki.banksystem.service;

import com.azki.banksystem.model.BankAccount;

public interface BankService {

    public BankAccount createAccount(BankAccount bankAccount) throws Exception;

    public BankAccount findByAccountNumber(String accountNumber) throws Exception;

    public void deposit(String accountNumber, Double amount) throws Exception;

    public void withdraw(String accountNumber, Double amount) throws Exception;

}
