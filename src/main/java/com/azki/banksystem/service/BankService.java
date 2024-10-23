package com.azki.banksystem.service;

import com.azki.banksystem.model.BankAccount;

public interface BankService {

    public BankAccount createAccount(String accountHolderName, double initialBalance) throws Exception;

}
