package pl.xavras.FoodOrder.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"restaurantId"})
@ToString(of = {"restaurantId", "name", "phone", "email"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private Set<MenuItemEntity> menuItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Set<RestaurantStreetEntity> restaurantStreets;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private Set<OrderEntity> orders;


}
