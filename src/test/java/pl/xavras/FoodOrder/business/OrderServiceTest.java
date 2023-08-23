package pl.xavras.FoodOrder.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.Address;
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
        // given
        MenuItemOrder menuItemOrder1 = someMenuItemOrder().withMenuItem(someMenuItem().withPrice(BigDecimal.TEN)).withQuantity(5);
        MenuItemOrder menuItemOrder2 = someMenuItemOrder().withMenuItem(someMenuItem().withPrice(BigDecimal.ONE)).withQuantity(5);

        Set<MenuItemOrder> menuItemOrders = new HashSet<>();
        menuItemOrders.add(menuItemOrder1);
        menuItemOrders.add(menuItemOrder2);

        // when
        BigDecimal totalValue = orderService.calculateTotalOrderValue(menuItemOrders);

        // then
        BigDecimal expectedTotal = new BigDecimal("55");
        assertEquals(expectedTotal, totalValue);
    }

    @Test
    public void testBuildOrder() {
        //given
        Address address = someAddress1();
        Set<MenuItemOrder> menuItemOrders = new HashSet<>();
        when(customerService.activeCustomer()).thenReturn(someCustomer());

        //when
        Order order = orderService.buildOrder(address, menuItemOrders);

        //then
        assertNotNull(order);
        assertNotNull(order.getOrderNumber());
        assertNotNull(order.getReceivedDateTime());
        assertEquals(address, order.getAddress());
        assertFalse(order.getCancelled());
        assertFalse(order.getCompleted());
        assertNotNull(order.getCustomer());
        assertEquals(menuItemOrders, order.getMenuItemOrders());
    }

    @Test
    void testGenerateOrderNumber() {
        // given
        OffsetDateTime when = OffsetDateTime.parse("2023-08-18T15:30:45Z");

        // when
        String orderNumber = orderService.generateOrderNumber(when);

        // then
        String expectedOrderNumberPattern = "\\d{4}\\.\\d{1,2}\\.\\d{1,2}-\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}\\.\\d{2}";
        assertTrue(orderNumber.matches(expectedOrderNumberPattern));
    }


    @Test
    void testIsCancellableWithinTimeFrame() {
        // given
        OffsetDateTime timeFromOrderMoment = OffsetDateTime.now().minus(10, ChronoUnit.MINUTES);
        Order order = someOrder().withReceivedDateTime(timeFromOrderMoment);

        // when
        boolean isCancellable = orderService.isCancellable(order);

        // then
        assertTrue(isCancellable);
    }

    @Test
    void testIsCancellableOutsideTimeFrame() {
        // given
        OffsetDateTime timeFromOrderMoment = OffsetDateTime.now().minus(21, ChronoUnit.MINUTES);
        Order order = someOrder().withReceivedDateTime(timeFromOrderMoment);

        // when
        boolean isCancellable = orderService.isCancellable(order);

        // then
        assertFalse(isCancellable);
    }
    @Test
    void testOrderStatusCompleted() {
        // given
        Order order = someOrder().withCompleted(true).withCancelled(false);

        // when
        String status = orderService.orderStatus(order);

        // then
        assertEquals("Completed", status);
    }

    @Test
    void testOrderStatusCancelled() {
        // given
        Order order = someOrder().withCancelled(true).withCompleted(false);

        // when
        String status = orderService.orderStatus(order);

        // then
        assertEquals("Cancelled", status);
    }

    @Test
    void testOrderStatusWaiting() {
        // given
        Order order = someOrder().withCancelled(false).withCompleted(false);

        // when
        String status = orderService.orderStatus(order);

        // then
        assertEquals("Waiting", status);
    }


    @Test
    void testFindByOrderNumberNonExistingOrder() {
        // given
        String orderNumber = "67890";
        when(orderDAO.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // when, then
        Throwable exception = assertThrows(NotFoundException.class, () -> orderService.findByOrderNumber(orderNumber));
        Assertions.assertEquals(String.format("Could not find order with orderNumber: [%s]", orderNumber), exception.getMessage());
    }

    @Test
    public void testFindByOrderNumberOrderExists() {
        // given
        String orderNumber = "123456";
        Order order = someOrder().withOrderNumber(orderNumber);
        when(orderDAO.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        // when
        Order result = orderService.findByOrderNumber(orderNumber);

        // then
        assertNotNull(result);
        assertEquals(orderNumber, (result.getOrderNumber()));
    }



}




