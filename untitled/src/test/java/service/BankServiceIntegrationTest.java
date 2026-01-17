package service;

import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;
import com.bank.service.BankService;
import com.bank.service.CustomerNotFoundException;
import com.bank.storage.BankStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankServiceIntegrationTest {

    @Autowired
    private BankService bankService;

    @Autowired
    private BankStorage bankStorage;

    @BeforeEach
    void setUp() {
        bankStorage.clear();
    }

    @Test
    void shouldRegisterAndRetrieveCustomer() {
        Customer customer = bankService.registerCustomer("Anna", 500.0);

        assertNotNull(customer.getId());
        assertEquals("Anna", customer.getName());
        assertEquals(500.0, customer.getBalance());

        Customer retrieved = bankService.getCustomerData(customer.getId());
        assertEquals(customer.getId(), retrieved.getId());
    }

    @Test
    void shouldDepositMoney() {
        Customer customer = bankService.registerCustomer("Piotr", 1000.0);

        Transaction result = bankService.depositMoney(customer.getId(), 300.0);

        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
        assertEquals(1300.0, result.getNewBalance());
    }

    @Test
    void shouldMakeTransferWhenBalanceSufficient() {
        Customer customer = bankService.registerCustomer("Maria", 1000.0);

        Transaction result = bankService.makeTransfer(customer.getId(), 400.0);

        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
        assertEquals(600.0, result.getNewBalance());
    }

    @Test
    void shouldDeclineTransferWhenBalanceInsufficient() {
        Customer customer = bankService.registerCustomer("Tomasz", 500.0);

        Transaction result = bankService.makeTransfer(customer.getId(), 1000.0);

        assertEquals(TransactionStatus.DECLINED, result.getStatus());
        assertEquals(500.0, result.getNewBalance());
    }

    @Test
    void shouldHandleCompleteWorkflow() {
        Customer customer = bankService.registerCustomer("Ewa", 1000.0);

        bankService.depositMoney(customer.getId(), 500.0);
        Customer after1 = bankService.getCustomerData(customer.getId());
        assertEquals(1500.0, after1.getBalance());

        bankService.makeTransfer(customer.getId(), 300.0);
        Customer after2 = bankService.getCustomerData(customer.getId());
        assertEquals(1200.0, after2.getBalance());

        Transaction declined = bankService.makeTransfer(customer.getId(), 2000.0);
        assertEquals(TransactionStatus.DECLINED, declined.getStatus());
        assertEquals(1200.0, declined.getNewBalance());
    }

    @Test
    void shouldThrowExceptionForNonExistentCustomer() {
        assertThrows(CustomerNotFoundException.class,
                () -> bankService.getCustomerData(999L));
    }
}

