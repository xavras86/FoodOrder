package pl.xavras.FoodOrder.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Integer> {
    Optional<RestaurantEntity> findByName(String name);

    @Query("""
            SELECT res FROM RestaurantEntity res
            WHERE res.address.street = :streetName
            """)
    Set<RestaurantEntity> findRestaurantByRestaurantStreetName(final @Param("streetName") String street);

    @Query("""
        SELECT res FROM RestaurantEntity res
        LEFT JOIN FETCH res.owner owner
        WHERE res.owner.email = :email
        """)
    Set<RestaurantEntity> findRestaurantsByOwner(final @Param("email") String email);


    Page<RestaurantEntity> findByOwner(Pageable pageable, OwnerEntity ownerEntity);

    Page <RestaurantEntity> findByName(String street, Pageable pageable);
}