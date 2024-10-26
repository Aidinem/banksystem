package com.azki.banksystem.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionLoggerImpl implements TransactionLogger {
    private final Logger logger = LoggerFactory.getLogger(TransactionLoggerImpl.class);

    @Override
    public void onTransaction(String accountNumber, String transactionType, Double amount) {
        logger.info("Transaction: {} {} {} {}", accountNumber, transactionType, amount, LocalDateTime.now());
    }
}
