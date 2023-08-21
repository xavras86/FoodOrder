package pl.xavras.FoodOrder.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@With
@Getter
@Setter
@EqualsAndHashCode(of = "orderNumber")
@ToString(of = {"orderId", "orderNumber", "receivedDateTime", "completedDateTime", "totalValue"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Integer orderId;

    @Column(name = "orders_number", unique = true) //todo dodać walidację
    private String orderNumber;

    @Column(name = "received_date_time")
    private OffsetDateTime receivedDateTime;

    @Column(name = "completed_date_time")
    private OffsetDateTime completedDateTime;

    @Column(name = "cancelled",columnDefinition = "boolean default false")
    private Boolean cancelled;

    @Column(name = "completed",columnDefinition = "boolean default false")
    private Boolean completed;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private Set<MenuItemOrderEntity> menuItemOrders;

}

