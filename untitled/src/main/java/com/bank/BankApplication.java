package com.bank;

import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(BankService bankService) {
        return args -> {
            System.out.println("\nBank deom\n");

            Customer customer = bankService.registerCustomer("Kasia Wirzba", 1000.0);
            System.out.println("Customer: " + customer.getName() + ", Balance: " + customer.getBalance());

            Transaction deposit = bankService.depositMoney(customer.getId(), 500.0);
            System.out.println("Deposit: " + deposit.getStatus() + ", New balance: " + deposit.getNewBalance());

            Transaction transfer1 = bankService.makeTransfer(customer.getId(), 200.0);
            System.out.println("Transfer 200: " + transfer1.getStatus() + ", New balance: " + transfer1.getNewBalance());

            Transaction transfer2 = bankService.makeTransfer(customer.getId(), 2000.0);
            System.out.println("Transfer 2000: " + transfer2.getStatus() + ", New balance: " + transfer2.getNewBalance());
        };
    }
}
