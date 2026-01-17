package com.bank.service;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;
import com.bank.storage.BankStorage;
import org.springframework.stereotype.Component;

@Component
public class BankService {
    private final BankStorage storage;

    public BankService(BankStorage storage) {
        this.storage = storage;
    }

    public Customer registerCustomer(String name, double balance) {
        // if (name.length() < 2) {
        //     throw new IllegalArgumentException("Nazwa za krótka");
        // }
        return storage.registerCustomer(name, balance);
    }

    public Customer getCustomerData(Long id) {
        Customer customer = storage.findById(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found: " + id);
        }
        return customer;
    }

    public Transaction makeTransfer(Long id, double amount) {
        Customer customer = getCustomerData(id);

        // if (amount > customer.getBalance()) {
    //      throw new Exception("Za mało kasy");
    // }



        if (customer.getBalance() < amount) {
            return new Transaction(TransactionStatus.DECLINED, customer.getBalance());
        }

        customer.setBalance(customer.getBalance() - amount);
        return new Transaction(TransactionStatus.ACCEPTED, customer.getBalance());
    }

    public Transaction depositMoney(Long id, double amount) {
        Customer customer = getCustomerData(id);
        customer.setBalance(customer.getBalance() + amount);
        return new Transaction(TransactionStatus.ACCEPTED, customer.getBalance());
    }




}
