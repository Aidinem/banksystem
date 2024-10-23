package com.azki.banksystem.log;

public interface TransactionLogger {

    void onTransaction(String accountNumber, String transactionType, double amount);
}
