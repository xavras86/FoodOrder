package pl.xavras.FoodOrder.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;
import pl.xavras.FoodOrder.util.DomainFixtures;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCalculateTotalOrderValue() {
        // Given
        MenuItemOrder menuItemOrder1 = someMenuItemOrder().withMenuItem(someMenuItem().withPrice(BigDecimal.TEN)).withQuantity(5);
        MenuItemOrder menuItemOrder2 = someMenuItemOrder().withMenuItem(someMenuItem().withPrice(BigDecimal.ONE)).withQuantity(5);

        Set<MenuItemOrder> menuItemOrders = new HashSet<>();
        menuItemOrders.add(menuItemOrder1);
        menuItemOrders.add(menuItemOrder2);

        // When
        BigDecimal totalValue = orderService.calculateTotalOrderValue(menuItemOrders);

        // Then
        BigDecimal expectedTotal = new BigDecimal("55");
        assertEquals(expectedTotal, totalValue);
    }

    @Test
    void testGenerateOrderNumber() {
        // Given
        OffsetDateTime when = OffsetDateTime.parse("2023-08-18T15:30:45Z");

        // When
        String orderNumber = orderService.generateOrderNumber(when);

        // Then
        String expectedOrderNumberPattern = "\\d{4}\\.\\d{1,2}\\.\\d{1,2}-\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}\\.\\d{2}";
        assertTrue(orderNumber.matches(expectedOrderNumberPattern));
    }


    @Test
    void testIsCancellableWithinTimeFrame() {
        // Given
        OffsetDateTime timeFromOrderMoment = OffsetDateTime.now().minus(10, ChronoUnit.MINUTES);
        Order order = DomainFixtures.someOrder().withReceivedDateTime(timeFromOrderMoment);

        // When
        boolean isCancellable = orderService.isCancellable(order);

        // Then
        assertTrue(isCancellable);
    }

    @Test
    void testIsCancellableOutsideTimeFrame() {
        // Given
        OffsetDateTime timeFromOrderMoment = OffsetDateTime.now().minus(21, ChronoUnit.MINUTES);
        Order order = DomainFixtures.someOrder().withReceivedDateTime(timeFromOrderMoment);

        // When
        boolean isCancellable = orderService.isCancellable(order);

        // Then
        assertFalse(isCancellable);
    }
    @Test
    void testOrderStatusCompleted() {
        // Given
        Order order = someOrder().withCompleted(true).withCancelled(false);

        // When
        String status = orderService.orderStatus(order);

        // Then
        assertEquals("Completed", status);
    }

    @Test
    void testOrderStatusCancelled() {
        // Given
        Order order = someOrder().withCancelled(true).withCompleted(false);

        // When
        String status = orderService.orderStatus(order);

        // Then
        assertEquals("Cancelled", status);
    }

    @Test
    void testOrderStatusWaiting() {
        // Given
        Order order = someOrder().withCancelled(false).withCompleted(false);

        // When
        String status = orderService.orderStatus(order);

        // Then
        assertEquals("Waiting", status);
    }


    @Test
    void testFindByOrderNumberNonExistingOrder() {
        // Given
        String orderNumber = "67890";
        when(orderDAO.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // When, Then
        Throwable exception = assertThrows(NotFoundException.class, () -> orderService.findByOrderNumber(orderNumber));
        Assertions.assertEquals(String.format("Could not find order with orderNumber: [%s]", orderNumber), exception.getMessage());
    }

    @Test
    public void testFindByOrderNumberOrderExists() {
        // Given
        String orderNumber = "123456";
        Order order = DomainFixtures.someOrder().withOrderNumber(orderNumber);
        when(orderDAO.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        // When
        Order result = orderService.findByOrderNumber(orderNumber);

        // Then
        assertNotNull(result);
        assertEquals(orderNumber, (result.getOrderNumber()));
    }



}




