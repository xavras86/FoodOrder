package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;

import java.util.Optional;
import java.util.Set;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Integer> {

    @Query("""
        SELECT ord FROM OrderEntity ord
        WHERE ord.customer.email = :email
        """)
    Set<OrderEntity> findOrdersByCustomerEmail(final @Param("email") String customerEmail);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("""
        SELECT ord FROM OrderEntity ord
        LEFT JOIN FETCH ord.restaurant rest
        LEFT JOIN FETCH rest.owner own
        WHERE own.email = :email
        """)
    Set<OrderEntity> findByOwnerEmail(final @Param("email") String email);
}

