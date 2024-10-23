package com.azki.banksystem.dao;

import com.azki.banksystem.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    BankAccount findByAccountNumber(String accountNumber);

    @Query("SELECT MAX(b.accountNumber) FROM BankAccount b")
    public String generateBankAccountNumber() throws Exception;
}
