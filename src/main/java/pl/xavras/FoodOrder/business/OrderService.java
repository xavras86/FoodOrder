package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private static final long MAX_CANCEL_SECONDS = 1200;

    private final OrderDAO orderDAO;
    private final CustomerService customerService;


    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    public Set<Order> findByCustomerEmail(String email) {
        return orderDAO.findOrdersByCustomerEmail(email);
    }

    public Page<Order> findOrdersByCustomerPaged(Pageable pageable, Customer activeCustomer) {
        return orderDAO.findOrdersByCustomerPaged(pageable, activeCustomer);
    }


    public Page<Order> findByCustomerAndCancelledAndCompletedPaged(Pageable pageable, Customer activeCustomer, Boolean cancelled, Boolean completed){
        return orderDAO.findByCustomerAndCancelledAndCompletedPaged(pageable, activeCustomer, cancelled, completed);
    }

    public Set<Order> findByOwnerEmail(String ownerEmail) {
        return orderDAO.findOrdersByOwnerEmail(ownerEmail);
    }
    public  Order findByOrderNumber(String orderNumber) {
        Optional<Order> byOrderNumber = orderDAO.findByOrderNumber(orderNumber);
        if (byOrderNumber.isEmpty()) {
            throw new NotFoundException("Could not find order with orderNumber: [%s]".formatted(orderNumber));
        }
        return byOrderNumber.get();
    }

    public void completeOrder(Order orderToComplete) {
        if ((orderToComplete.getCompleted() && Objects.nonNull(orderToComplete.getCompletedDateTime()))
                || orderToComplete.getCancelled()) {
            return;
        }
        orderDAO.completeOrder(orderToComplete);
    }

    public void cancelOrder(Order order) {
        if (order.getCancelled() && !isCancellable(order)) {
            return;
        }
        orderDAO.cancelOrder(order);
    }

    public Boolean isCancellable(Order order) {
        return Duration.between(order.getReceivedDateTime(), OffsetDateTime.now()).toSeconds() <= MAX_CANCEL_SECONDS;
    }


    public Order placeOrder(Order order, String restaurantName, Set<MenuItemOrder> menuItemOrders) {
        Address deliveryAddress = order.getAddress();

        Order orderToPlace = buildOrder(deliveryAddress, menuItemOrders);
        return orderDAO.saveOrder(orderToPlace, restaurantName, menuItemOrders);
    }

    public String orderStatus(Order order) {
        return order.getCancelled() ? "Cancelled" : (order.getCompleted() ? "Completed" : "Waiting");
    }


    private Order buildOrder(Address address,
                             Set<MenuItemOrder> menuItemOrders
    ) {
        OffsetDateTime whenCreated = OffsetDateTime.now();
        return Order.builder()
                .orderNumber(generateOrderNumber(whenCreated))
                .receivedDateTime(whenCreated)
                .totalValue(calculateTotalOrderValue(menuItemOrders))
                .address(address)
                .cancelled(false)
                .completed(false)
                .customer(customerService.activeCustomer())
                .build();
    }

    private BigDecimal calculateTotalOrderValue(Set<MenuItemOrder> menuItemOrders) {
        return menuItemOrders.stream()
                .map(this::calculateOrderEntryValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal calculateOrderEntryValue(MenuItemOrder menuItemOrder) {
        return menuItemOrder.getMenuItem().getPrice()
                .multiply(new BigDecimal(menuItemOrder.getQuantity()));
    }


    private String generateOrderNumber(OffsetDateTime when) {
        return "%s.%s.%s-%s.%s.%s.%s".formatted(
                when.getYear(),
                when.getMonth().getValue(),
                when.getDayOfMonth(),
                when.getHour(),
                when.getMinute(),
                when.getSecond(),
                (new Random().nextInt(90) + 10)
        );
    }

}