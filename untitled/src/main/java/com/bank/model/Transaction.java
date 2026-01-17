package com.bank.model;

public class Transaction {
    private TransactionStatus status;
    private double newBalance;

    public Transaction(TransactionStatus status, double newBalance) {
        this.status = status;
        this.newBalance = newBalance;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public double getNewBalance() {
        return newBalance;
    }
}
