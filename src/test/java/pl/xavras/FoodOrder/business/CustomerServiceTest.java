package pl.xavras.FoodOrder.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.business.dao.CustomerDAO;
import pl.xavras.FoodOrder.domain.Customer;
import jakarta.persistence.EntityNotFoundException;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.EntityFixtures;

import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerDAO customerDAO;


    @Test
    public void testFindAll() {
        // Given
        List<Customer> customers = List.of(
                someCustomer().withCustomerId(555).withName("Jan"),
                someCustomer().withCustomerId(444).withName("Janina")
        );
        when(customerDAO.findAll()).thenReturn(customers);

        // When
        List<Customer> result = customerService.findAll();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByEmailExistingCustomer() {
        // Given
        Customer customer = someCustomer().withName("Jan").withEmail("jan@customer.pl");
        when(customerDAO.findByEmail("jan@customer.pl")).thenReturn(Optional.of(customer));

        // When
        Customer result = customerService.findByEmail("jan@customer.pl");

        // Then
        assertNotNull(result);
        assertEquals("Jan", result.getName());
    }

    @Test
    public void testFindByEmailNonExistingCustomer() {
        // Given
        when(customerDAO.findByEmail("nonexistent@customer.com")).thenReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> customerService.findByEmail("nonexistent@customer.com"));
    }

    @Test
    public void testSaveCustomer() {
        // Given
        Customer customer = someCustomer().withName("Jan").withEmail("jan@customer.pl");
        when(customerDAO.saveCustomer(customer)).thenReturn(customer);

        // When
        Customer result = customerService.saveCustomer(customer);

        // Then
        assertNotNull(result);
        assertEquals("jan@customer.pl", result.getEmail());
        verify(customerDAO, times(1)).saveCustomer(customer);
    }

    @Test
    public void testActiveCustomer() {
        // Given
        Customer customer = someCustomer().withName("Jan").withEmail("jan@customer.pl");
        when(customerDAO.findLoggedCustomer()).thenReturn(customer);

        // When
        Customer result = customerService.activeCustomer();

        // Then
        assertNotNull(result);
        assertEquals("Jan", result.getName());
    }
}