package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.ZonedDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderDAO orderDAO;
    private static final long MAX_CANCEL_SECONDS = 1200;

    private final CustomerService customerService;
    private final AddressService addressService;

    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    public Set<Order> findByCustomerEmail(String email) {
        return orderDAO.findOrdersByCustomerEmail(email);
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        Optional<Order> byOrderNumber = orderDAO.findByOrderNumber(orderNumber);
        if (byOrderNumber.isEmpty()) {
            throw new NotFoundException("Could not find order with orderNumber: [%s]".formatted(orderNumber));
        }
        return byOrderNumber;
    }


    public void cancelOrder(Order order) {
        if(order.getCancelled() && !isCancellable(order)){
            return;
        }
        orderDAO.cancelOrder(order);
    }

    public Boolean isCancellable(Order order) {

        Duration between = Duration.between(order.getReceivedDateTime(), OffsetDateTime.now());
        long seconds = between.getSeconds();

        boolean b = Duration.between(order.getReceivedDateTime(), OffsetDateTime.now()).toSeconds() <= MAX_CANCEL_SECONDS;

        return b;


    }

    public Order placeOrder(Order order, Set<MenuItemOrder> menuItemOrders) {
        Address deliveryAddress = order.getAddress();

        Order orderToPlace = buildOrder(deliveryAddress, menuItemOrders);
        return orderDAO.saveOrder(orderToPlace, menuItemOrders);
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