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
import java.time.OffsetDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderDAO orderDAO;


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


    public Order placeOrder(Order order, Set<MenuItemOrder> menuItemOrders) {
        Address deliveryAddress = order.getAddress();
        Order orderToPlace = buildOrder( deliveryAddress, menuItemOrders);
        return orderDAO.saveOrder(orderToPlace);
    }

    private Order buildOrder(Address address,
                             Set<MenuItemOrder> menuItemOrders
                             //todo powinno być zalezne od restauracji - zamowienie tylko z jednej restauracji, czy tzeba tutaj uwzglednic ?
    ) {
        OffsetDateTime whenCreated = OffsetDateTime.now();
        return Order.builder()
                .orderNumber(generateOrderNumber(whenCreated))
                .receivedDateTime(whenCreated)
                .completedDateTime(null) //todo dodac date realizacji
                .totalValue(calculateTotalOrderValue(menuItemOrders))
                .address(address)
                .customer(customerService.activeCustomer())
                .menuItemOrders(menuItemOrders)
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