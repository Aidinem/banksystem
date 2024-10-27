package com.azki.banksystem.log;

public interface TransactionObserver {

    void onTransaction(String accountNumber, String transactionType, Double amount);

    public void addObserver(TransactionObserver observer);

    public void notifyObservers(String accountNumber,String transactionType, Double amount);
}
