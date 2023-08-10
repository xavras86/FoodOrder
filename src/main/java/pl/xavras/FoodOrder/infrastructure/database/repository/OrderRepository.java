package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.AddressEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.MenuItemOrderEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.OrderEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository implements OrderDAO {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    private final AddressRepository addressRepository;
    private final MenuItemOrderEntityMapper menuItemOrderEntityMapper;
    private final AddressEntityMapper addressEntityMapper;


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
                .orElseThrow(() -> new RuntimeException("Could not find restaurant with name: [%s]"
                .formatted(restaurantName)));  //chyba lepiej cały obiekt niz sama string przekazywac do przemyslenia

        //weryfikacja czy przekazany adres jest już w bazie
        Optional<AddressEntity> existingAddress = addressRepository.findExistingAddress(order.getAddress());
        if (existingAddress.isPresent()) {
            toSave.setAddress(existingAddress.get());
        } else {
            toSave.setAddress(addressEntityMapper.mapToEntity(order.getAddress()));
        }

        toSave.setRestaurant(restaurant);
        Set<MenuItemOrderEntity> menuItemOrderEntities = menuItemOrderEntityMapper.mapToEntity(menuItemOrders);
        menuItemOrderEntities.forEach(a -> a.setOrder(toSave));
        toSave.setMenuItemOrders(menuItemOrderEntities);
        OrderEntity save = orderJpaRepository.save(toSave);
        return orderEntityMapper.mapFromEntity(save);
    }

    @Override
    public void cancelOrder(Order order) {
        OrderEntity orderEntity = orderJpaRepository.findByOrderNumber(order.getOrderNumber())
                .orElseThrow(() -> new RuntimeException("Could not find order with orderNumber: [%s]"
                .formatted(order.getOrderNumber())));
        orderEntity.setCancelled(true);
        orderJpaRepository.save(orderEntity);
    }

    @Override
    public void completeOrder(Order order) {
        OrderEntity orderEntity = orderJpaRepository.findByOrderNumber(order.getOrderNumber())
                .orElseThrow(() -> new RuntimeException("Could not find order with orderNumber: [%s]"
                        .formatted(order.getOrderNumber())));
        orderEntity.setCompleted(true);
        orderEntity.setCompletedDateTime(OffsetDateTime.now());
        orderJpaRepository.save(orderEntity);
    }


}
