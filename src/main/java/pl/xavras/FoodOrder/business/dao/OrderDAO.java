package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderDAO {

    List<Order> findAll();

    Set<Order> findOrdersByCustomerEmail(String customerEmail);

    Optional<Order> findByOrderNumber(String orderNumber);

    Set<Order> findOrdersByOwnerEmail(String ownerEmail);

    Order saveOrder(Order order, String restaurantName, Set<MenuItemOrder> menuItemOrders);

    void cancelOrder(Order order);

    void completeOrder(Order order);






}