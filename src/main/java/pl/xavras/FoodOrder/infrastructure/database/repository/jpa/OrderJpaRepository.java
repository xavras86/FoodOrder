package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;

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


    Page<OrderEntity> findByCustomer(Pageable pageable, CustomerEntity customerEntity);


    Page<OrderEntity> findByCustomerAndCancelledAndCompleted(Pageable pageable, CustomerEntity customer, boolean cancelled, boolean completed);

}

