package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Integer> {
    Optional<RestaurantEntity> findByName(String name);

    @Query("""
            SELECT res FROM RestaurantEntity res
            WHERE res.address.street = :streetName
            """)
    Set<RestaurantEntity> findRestaurantByRestaurantStreetName(final @Param("streetName") String street);

}