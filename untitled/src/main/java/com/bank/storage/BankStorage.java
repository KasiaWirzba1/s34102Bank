package com.bank.storage;

import com.bank.model.Customer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankStorage {
    private final Map<Long, Customer> customers = new HashMap<>();
    private Long nextId = 1L;

    public Customer registerCustomer(String name, double balance) {
        Customer customer = new Customer(nextId++, name, balance);
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer findById(Long id) {
        return customers.get(id);
    }


    public void clear() {
        customers.clear();
        nextId = 1L;
    }
}
        /*
    public int getCustomerCount() {
            return customers.size();
    */

