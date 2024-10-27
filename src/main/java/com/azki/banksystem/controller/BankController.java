
package com.azki.banksystem.controller;

import com.azki.banksystem.model.BankAccount;
import com.azki.banksystem.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banksystem")
public class BankController {
    // Dear AZKIVAM friend,
    // This Controller class is just for test create And display Account API
    // with postman And it's not completed

    @Autowired
    BankService bankService;

    @PostMapping("/createAccount")
    public void createAccount(@RequestBody BankAccount bankAccount) throws Exception {
            bankService.createAccount(bankAccount);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccount> displayAccountInfo(@PathVariable String accountNumber) throws Exception {
            BankAccount bankAccount = bankService.findByAccountNumber(accountNumber);
            return ResponseEntity.ok(bankAccount);
    }
}

