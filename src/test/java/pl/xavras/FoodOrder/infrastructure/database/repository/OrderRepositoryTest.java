package pl.xavras.FoodOrder.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OrderJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.*;
import pl.xavras.FoodOrder.util.EntityFixtures;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {


    @Mock
    private OrderJpaRepository orderJpaRepository;
    @Mock
    private OrderEntityMapper orderEntityMapper;
    @Mock
    private MenuItemOrderEntityMapper menuItemOrderEntityMapper;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressEntityMapper addressEntityMapper;
    @Mock
    private CustomerEntityMapper customerEntityMapper;
    @Mock
    private OwnerEntityMapper ownerEntityMapper;
    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;
    @InjectMocks
    private OrderRepository orderRepository;

    @Test
    void testFindAll() {
        // Given
        OrderEntity orderEntity = EntityFixtures.someOrderEntity();
        when(orderJpaRepository.findAll()).thenReturn(List.of(orderEntity));
        when(orderEntityMapper.mapFromEntity(orderEntity)).thenReturn(someOrder());

        // When
        List<Order> result = orderRepository.findAll();

        // Then
        assertEquals(1, result.size());
        verify(orderJpaRepository, times(1)).findAll();
        verify(orderEntityMapper, times(1)).mapFromEntity(orderEntity);
    }

    @Test
    void testFindOrdersByCustomerEmail() {
        // Given
        String customerEmail = "customer@example.com";
        OrderEntity orderEntity = EntityFixtures.someOrderEntity();
        when(orderJpaRepository.findOrdersByCustomerEmail(customerEmail)).thenReturn(Set.of(orderEntity));
        when(orderEntityMapper.mapFromEntity(orderEntity)).thenReturn(someOrder());

        // When
        Set<Order> result = orderRepository.findOrdersByCustomerEmail(customerEmail);

        // Then
        assertEquals(1, result.size());
        verify(orderJpaRepository, times(1)).findOrdersByCustomerEmail(customerEmail);
        verify(orderEntityMapper, times(1)).mapFromEntity(orderEntity);
    }

    @Test
    void testCancelOrder() {
        // Given
        Order order = someOrder();
        OrderEntity orderEntity = Mockito.mock(OrderEntity.class);
        when(orderJpaRepository.findByOrderNumber(order.getOrderNumber())).thenReturn(Optional.of(orderEntity));

        // When
        orderRepository.cancelOrder(order);

        // Then
        verify(orderJpaRepository, times(1)).findByOrderNumber(order.getOrderNumber());
        verify(orderEntity, times(1)).setCancelled(true);
        verify(orderJpaRepository, times(1)).save(orderEntity);
    }

    
    @Test
    void testCompleteOrder() {
        // Given
        Order order = someOrder();
        OrderEntity orderEntity = Mockito.mock(OrderEntity.class);
        when(orderJpaRepository.findByOrderNumber(order.getOrderNumber())).thenReturn(Optional.of(orderEntity));

        // When
        orderRepository.completeOrder(order);

        // Then
        verify(orderJpaRepository, times(1)).findByOrderNumber(order.getOrderNumber());
        verify(orderEntity, times(1)).setCompleted(true);
        verify(orderEntity, times(1)).setCompletedDateTime(any(OffsetDateTime.class));
        verify(orderJpaRepository, times(1)).save(orderEntity);
    }

    @Test
    void testGetOrderEntityByOrder() {
        // Given
        Order order = someOrder();
        OrderEntity orderEntity = EntityFixtures.someOrderEntity();
        when(orderJpaRepository.findByOrderNumber(order.getOrderNumber())).thenReturn(Optional.of(orderEntity));

        // When
        OrderEntity result = orderRepository.getOrderEntityByOrder(order);

        // Then
        verify(orderJpaRepository, times(1)).findByOrderNumber(order.getOrderNumber());
        assertEquals(orderEntity, result);
    }

    @Test
    void testGetOrderEntityByOrderNotFound() {
        // Given
        Order order = someOrder();
        when(orderJpaRepository.findByOrderNumber(order.getOrderNumber())).thenReturn(Optional.empty());

        // When and Then

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> orderRepository.getOrderEntityByOrder(order));
        Assertions.assertEquals(("Could not find order with orderNumber: [%s]".formatted(order.getOrderNumber())),
                exception.getMessage());
    }
}