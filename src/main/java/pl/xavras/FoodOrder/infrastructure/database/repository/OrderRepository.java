package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.api.dto.mapper.MenuItemOrderMapper;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.MenuItemOrderEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.OrderEntityMapper;

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

    private final MenuItemOrderEntityMapper menuItemOrderEntityMapper;


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
    public Order saveOrder(Order order, Set<MenuItemOrder> menuItemOrders) {

        OrderEntity toSave = orderEntityMapper.mapToEntity(order);
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


}
