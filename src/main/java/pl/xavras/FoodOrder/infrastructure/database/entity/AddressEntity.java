package pl.xavras.FoodOrder.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "country", "city", "street", "buildingNumber"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address",cascade = CascadeType.ALL)
    private RestaurantEntity restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    private Set<OrderEntity> orders;
}
