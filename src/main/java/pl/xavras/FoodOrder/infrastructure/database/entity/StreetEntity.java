package pl.xavras.FoodOrder.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@With
@Getter
@Setter
@EqualsAndHashCode(of = "streetId")
@ToString(of ={"streetId", "city", "streetName"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "street")
public class StreetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "street_id")
    private Integer streetId;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String streetName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "street")
    @Fetch(value = FetchMode.JOIN)
    private Set<RestaurantStreetEntity> restaurantStreets;
}
