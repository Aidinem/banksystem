package com.azki.banksystem.controller;

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
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Scanner scanner = new Scanner(System.in);


    @Override
    public void run(String... args) throws Exception {
        while (true) {
            displayBankSystemOptions();
            Integer selectedOption = scanner.nextInt();
            handleSelectedOption(selectedOption);
        }
    }

    public void displayBankSystemOptions() {
        System.out.println("--- Welcome to AzkiVam Banking System ---");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Check Balance");
        System.out.println("6. Exit");
        System.out.println("Choose an option: ");
    }

    public void handleSelectedOption(Integer selectedOption) throws Exception {
        switch (selectedOption) {
            case 1 -> createAccount();
            case 2 -> deposit();
            case 3 -> withdraw();
            case 4 -> transfer();
            case 5 -> checkBalance();
            case 6 -> System.out.println("Exiting the Banking System. Goodbye!");
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private void createAccount() {
        System.out.print("Enter account holder name: ");
        String holderName = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        Double initialBalance = scanner.nextDouble();
        BankAccount bankAccount = new BankAccount(holderName,initialBalance);
        executorService.submit(() -> {
            BankAccount account;
            try {
                account = bankService.createAccount(bankAccount);
                System.out.println("Account created successfully! Account Number: " + account.getAccountNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void deposit() throws Exception {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter amount to deposit: ");
        Double amount = scanner.nextDouble();

        bankService.deposit(accountNumber, amount);
        System.out.println("Deposit successful!");
    }

    private void withdraw() throws Exception {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter amount to withdraw: ");
        Double amount = scanner.nextDouble();

        bankService.withdraw(accountNumber, amount);
        System.out.println("Withdrawal successful!");
    }

    private void transfer() {
        System.out.print("Enter sender's account number: ");
        String fromAccount = scanner.nextLine();
        System.out.print("Enter receiver's account number: ");
        String toAccount = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        Double amount = scanner.nextDouble();

/*        bank.transfer(fromAccount, toAccount, amount);
        System.out.println("Transfer successful!");*/
    }

    private void checkBalance() throws Exception {
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