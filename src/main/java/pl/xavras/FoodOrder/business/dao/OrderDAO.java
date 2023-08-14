package pl.xavras.FoodOrder.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.xavras.FoodOrder.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderDAO {

    List<Order> findAll();

    Set<Order> findOrdersByCustomerEmail(String customerEmail);

    Page<Order> findOrdersByCustomerPaged(Pageable pageable, Customer activeCustomer);

    Page<Order> findByCustomerAndCancelledAndCompletedPaged(Pageable pageable, Customer customer, boolean cancelled, boolean completed);

    Optional<Order> findByOrderNumber(String orderNumber);

    Set<Order> findOrdersByOwnerEmail(String ownerEmail);

    Order saveOrder(Order order, String restaurantName, Set<MenuItemOrder> menuItemOrders);

    void cancelOrder(Order order);

    void completeOrder(Order order);

    Page<Order> findByOwnerAndCancelledAndCompletedPaged(Pageable pageable, Owner activeOwner, Boolean cancelled, Boolean completed);
}