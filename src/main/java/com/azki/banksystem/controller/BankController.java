package com.azki.banksystem.controller;

import com.azki.banksystem.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    @Autowired
    BankService bankService;

    @RequestMapping("/createAccount")
    public void createAccount(String accountHolderName,Double initialBalance){
        try {
            bankService.createAccount(accountHolderName,initialBalance);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
