package com.azki.banksystem.log;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionLogger implements TransactionObserver {

    private List<TransactionObserver> observers = new ArrayList<>();

    @Override
    public void onTransaction(String accountNumber, String transactionType, Double amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.log", true))) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            writer.write(String.format("Account: %s | Transaction: %s | Amount: %.2f%n | Time: %S",
                    accountNumber, transactionType, amount, dateTimeFormatter.format(now)));
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    @Override
    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(String accountNumber, String transactionType, Double amount) {
        for (TransactionObserver observer : observers) {
            observer.onTransaction(accountNumber, transactionType, amount);
        }
    }
}
