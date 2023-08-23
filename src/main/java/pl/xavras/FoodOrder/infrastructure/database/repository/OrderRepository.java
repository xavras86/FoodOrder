package pl.xavras.FoodOrder.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.infrastructure.database.entity.*;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository implements OrderDAO {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final AddressRepository addressRepository;
    private final MenuItemOrderEntityMapper menuItemOrderEntityMapper;
    private final AddressEntityMapper addressEntityMapper;

    private final CustomerEntityMapper customerEntityMapper;
    private final OwnerEntityMapper ownerEntityMapper;

    private final RestaurantJpaRepository restaurantJpaRepository;


    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(orderEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Set<Order> findOrdersByCustomerEmail(String customerEmail) {
        return orderJpaRepository.findOrdersByCustomerEmail(customerEmail).stream()
                .map(orderEntityMapper::mapFromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public Page<Order> findOrdersByCustomerPaged(Pageable pageable, Customer activeCustomer) {
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(activeCustomer);

        Page<OrderEntity> byActiveCustomer =  orderJpaRepository.findByCustomer(pageable, customerEntity);
        return byActiveCustomer.map(orderEntityMapper::mapFromEntity);
    }
    @Override
    public Page<Order> findByCustomerAndCancelledAndCompletedPaged(
            Pageable pageable, Customer customer, boolean cancelled, boolean completed) {
        CustomerEntity customerEntity = customerEntityMapper.mapToEntity(customer);
        Page<OrderEntity> byActiveCustomerAndFlags =  orderJpaRepository.findByCustomerAndCancelledAndCompleted(
                pageable, customerEntity,cancelled, completed);
        return byActiveCustomerAndFlags.map(orderEntityMapper::mapFromEntity);
    }


    @Override
    public Page<Order> findByOwnerAndCancelledAndCompletedPaged(
            Pageable pageable, Owner owner, Boolean cancelled, Boolean completed) {
        OwnerEntity ownerEntity = ownerEntityMapper.mapToEntity(owner);
        Page<OrderEntity> byActiveOwnerAndFlags =  orderJpaRepository.findByRestaurantOwnerAndCancelledAndCompleted(
                pageable, ownerEntity, cancelled,completed);
        return byActiveOwnerAndFlags.map(orderEntityMapper::mapFromEntity);
    }

    @Override
    public Set<Order> findByRestaurantName(String restaurantName) {
        return orderJpaRepository.findByRestaurantName(restaurantName).stream()
                .map(orderEntityMapper::mapFromEntity).collect(Collectors.toSet());
    }

    @Override
    public void deleteByOrderNumber(String orderNumber) {
        orderJpaRepository.deleteByOrderNumber(orderNumber);
    }

    @Override
    public Set<MenuItemOrder> findMenuItemOrdersByOrder(Order order) {
        OrderEntity orderEntity = getOrderEntityByOrder(order);
        return orderEntity.getMenuItemOrders().stream().map(menuItemOrderEntityMapper::mapFromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber)
                .map(orderEntityMapper::mapFromEntity);
    }

    @Override
    public Set<Order> findOrdersByOwnerEmail(String ownerEmail) {
        return orderJpaRepository.findByOwnerEmail(ownerEmail).stream()
                .map(orderEntityMapper::mapFromEntity)
                .collect(Collectors.toSet());
    }


    @Override
    public Order saveOrder(Order order, String restaurantName, Set<MenuItemOrder> menuItemOrders) {

        OrderEntity toSave = orderEntityMapper.mapToEntity(order);
        RestaurantEntity restaurant = restaurantJpaRepository.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("Could not find restaurant with name: [%s]"
                .formatted(restaurantName)));

        //weryfikacja czy przekazany adres jest już w bazie
        AddressEntity address = addressRepository.findExistingAddress(order.getAddress())
                .orElseGet(() -> addressEntityMapper.mapToEntity(order.getAddress()));

        toSave.setAddress(address);
        toSave.setRestaurant(restaurant);
        Set<MenuItemOrderEntity> menuItemOrderEntities = menuItemOrders.stream()
                .map(menuItemOrderEntityMapper::mapToEntity)
                .collect(Collectors.toSet());
        menuItemOrderEntities.forEach(a -> a.setOrder(toSave));
        toSave.setMenuItemOrders(menuItemOrderEntities);
        OrderEntity save = orderJpaRepository.save(toSave);
        log.info("Created new Order Entity: "+ save.getOrderNumber());
        return orderEntityMapper.mapFromEntity(save);
    }

    @Override
    public void cancelOrder(Order order) {
        OrderEntity orderEntity = getOrderEntityByOrder(order);
        orderEntity.setCancelled(true);
        orderJpaRepository.save(orderEntity);
    }

    @Override
    public void completeOrder(Order order) {
        OrderEntity orderEntity = getOrderEntityByOrder(order);
        orderEntity.setCompleted(true);
        orderEntity.setCompletedDateTime(OffsetDateTime.now());
        orderJpaRepository.save(orderEntity);
    }


    OrderEntity getOrderEntityByOrder(Order order) {
        return orderJpaRepository.findByOrderNumber(order.getOrderNumber())
                .orElseThrow(() -> new EntityNotFoundException("Could not find order with orderNumber: [%s]"
                        .formatted(order.getOrderNumber())));
    }

}
