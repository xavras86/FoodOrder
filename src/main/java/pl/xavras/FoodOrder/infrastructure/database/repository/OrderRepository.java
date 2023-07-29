package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OrderJpaRepository;
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
    public Order saveOrder(Order order) {

        OrderEntity toSave = orderEntityMapper.mapToEntity(order);
        OrderEntity saved = orderJpaRepository.saveAndFlush(toSave);
        return orderEntityMapper.mapFromEntity(saved);
    }


}
