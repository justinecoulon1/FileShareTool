package com.justicou.file.share.tool.websocket.transactions;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileTransactionsService {
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private final Map<Long, Transaction> transactionById = new HashMap<>();

    public Transaction createTransaction(Long downloadingUserId, Long uploadingUserId, String fileName) {
        var transaction = new Transaction(ID_GENERATOR.incrementAndGet(), downloadingUserId, uploadingUserId, fileName);
        transactionById.put(transaction.id(), transaction);
        return transaction;
    }

    public Transaction getTransactionById(Long id) {
        return transactionById.get(id);
    }

    public Transaction completeTransaction(Long id) {
        return transactionById.remove(id);
    }

}
