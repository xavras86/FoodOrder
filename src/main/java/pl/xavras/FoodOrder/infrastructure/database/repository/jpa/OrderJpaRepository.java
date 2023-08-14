package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;

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


    @Query("""
            SELECT ord FROM OrderEntity ord
                LEFT JOIN ord.restaurant rest
                LEFT JOIN rest.owner own
                WHERE own = :owner
                AND ord.cancelled = :cancelled
                AND ord.completed = :completed
           """)
    Page<OrderEntity> findByOwnerAndCancelledAndCompleted(Pageable pageable, final @Param("owner") OwnerEntity owner,
                                                          final @Param("cancelled") boolean cancelled,
                                                          final @Param("completed") boolean completed
    );


    Page<OrderEntity> findByCustomer(Pageable pageable, CustomerEntity customerEntity);


    Page<OrderEntity> findByCustomerAndCancelledAndCompleted(Pageable pageable, CustomerEntity customer, boolean cancelled, boolean completed);

    Page<OrderEntity> findByRestaurantOwnerAndCancelledAndCompleted(Pageable pageable, OwnerEntity customer, boolean cancelled, boolean completed);

}

