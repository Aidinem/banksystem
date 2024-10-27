package com.azki.banksystem.controller;

import com.azki.banksystem.log.TransactionLogger;
import com.azki.banksystem.log.TransactionObserver;
import com.azki.banksystem.model.BankAccount;
import com.azki.banksystem.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BankSystemConsoleUI implements CommandLineRunner {

    @Autowired
    private BankService bankService;
    @Autowired
    private TransactionObserver transactionObserver;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    Scanner scanner = new Scanner(System.in);


    @Override
    public void run(String... args) throws Exception {
        transactionObserver.addObserver(new TransactionLogger());
        while (true) {
            displayBankSystemOptions();
            String selectedOption = scanner.nextLine();
            handleSelectedOption(selectedOption.trim());
        }
    }

    public void displayBankSystemOptions() {
        System.out.println("--- Welcome to AzkiVam Banking System ---");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Display Account Information");
        System.out.println("6. Exit");
        System.out.println("Choose an option: ");
    }

    public void handleSelectedOption(String selectedOption) throws Exception {
        try {
            switch (Integer.parseInt(selectedOption)) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> transfer();
                case 5 -> displayAccountIfo();
                case 6 -> System.out.println("Exiting the Banking System. Goodbye!");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println(selectedOption.isEmpty() ? "Selected Option Can not be null" : selectedOption + " is not a valid integer");
        }

    }

    private void createAccount() {
        scanner = new Scanner(System.in);
        System.out.println("Enter account holder name: ");
        String holderName = scanner.nextLine();
        System.out.println("Enter initial balance: ");
        Double initialBalance = scanner.nextDouble();
        BankAccount bankAccount = new BankAccount(holderName, initialBalance);
        executorService.submit(() -> {
            BankAccount account;
            try {
                account = bankService.createAccount(bankAccount);
                System.out.println("Account created successfully! Account Number: " + account.getAccountNumber());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void deposit() {
        scanner = new Scanner(System.in);
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter amount to deposit: ");
        Double amount = scanner.nextDouble();

        executorService.submit(() -> {
            try {
                bankService.deposit(accountNumber, amount);
                System.out.println("Deposit successful!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void withdraw() {
        scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter amount to withdraw: ");
        Double amount = scanner.nextDouble();

        executorService.submit(() -> {
            try {
                bankService.withdraw(accountNumber, amount);
                System.out.println("Withdrawal successful!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void transfer() {
        scanner = new Scanner(System.in);
        System.out.print("Enter sender's account number: ");
        String fromAccount = scanner.nextLine();
        System.out.print("Enter receiver's account number: ");
        String toAccount = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        Double amount = scanner.nextDouble();

        executorService.submit(() -> {
            try {
                bankService.transfer(fromAccount, toAccount, amount);
                System.out.println("Transfer successful!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void displayAccountIfo() throws Exception {
        scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        BankAccount bankAccount = bankService.findByAccountNumber(accountNumber);
        if (bankAccount != null && bankAccount.getId() > 0) {
            System.out.println("Account Holder Name: " + bankAccount.getAccountHolderName() + " And Current balance for account " + accountNumber + ": " + bankAccount.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }
}