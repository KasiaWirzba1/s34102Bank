package service;

import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;
import com.bank.service.BankService;
import com.bank.service.CustomerNotFoundException;
import com.bank.storage.BankStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private BankStorage storage;

    @InjectMocks
    private BankService service;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Jan", 1000.0);
    }

    @Test
    void shouldRegisterCustomer() {
        when(storage.registerCustomer("Jan", 1000.0)).thenReturn(customer);

        Customer result = service.registerCustomer("Jan", 1000.0);

        assertEquals("Jan", result.getName());
        assertEquals(1000.0, result.getBalance());
        verify(storage).registerCustomer("Jan", 1000.0);
    }

    @Test
    void shouldGetCustomerData() {
        when(storage.findById(1L)).thenReturn(customer);

        Customer result = service.getCustomerData(1L);

        assertEquals(1L, result.getId());
        verify(storage).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(storage.findById(999L)).thenReturn(null);

        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerData(999L));
    }

    @Test
    void shouldAcceptTransferWhenBalanceSufficient() {
        when(storage.findById(1L)).thenReturn(customer);

        Transaction result = service.makeTransfer(1L, 500.0);

        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
        assertEquals(500.0, result.getNewBalance());
    }

    @Test
    void shouldDeclineTransferWhenBalanceInsufficient() {
        when(storage.findById(1L)).thenReturn(customer);

        Transaction result = service.makeTransfer(1L, 2000.0);

        assertEquals(TransactionStatus.DECLINED, result.getStatus());
        assertEquals(1000.0, result.getNewBalance());
    }

    @Test
    void shouldDepositMoney() {
        when(storage.findById(1L)).thenReturn(customer);

        Transaction result = service.depositMoney(1L, 500.0);

        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
        assertEquals(1500.0, result.getNewBalance());
    }
}

/*
@Test
void testUjemnjeKwoty() {
    when(storage.findById(1L)).thenReturn(customer);

    assertThrows(IllegalArgumentException.class,
        () -> service.makeTransfer(1L, -100.0));
}
*/