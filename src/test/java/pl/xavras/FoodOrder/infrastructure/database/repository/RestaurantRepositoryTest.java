package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.StreetEntityMapper;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.EntityFixtures;
import pl.xavras.FoodOrder.util.integration.configuration.AbstractIT;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static pl.xavras.FoodOrder.util.EntityFixtures.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class RestaurantRepositoryTest extends AbstractIT {

    private RestaurantJpaRepository restaurantJpaRepository;
    private RestaurantRepository restaurantRepository;

    private RestaurantEntityMapper restaurantEntityMapper;

    private StreetEntityMapper streetEntityMapper;

    @Test
    void testThatCreateAndFindByNameWorksCorrectly() {

        //given
        Restaurant restaurant = DomainFixtures.someRestaurant1().withName("Some Restaurant");

        //when
        Restaurant saved = restaurantRepository.createNewRestaurant(restaurant);
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByName(saved.getName());

        //then
        assertThat(foundRestaurant).isPresent();
        assertThat(foundRestaurant.get().getName()).isEqualTo("Some Restaurant");
    }


}
