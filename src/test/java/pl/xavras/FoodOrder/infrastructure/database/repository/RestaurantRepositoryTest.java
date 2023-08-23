package pl.xavras.FoodOrder.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.EntityFixtures;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryTest {

    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;

    @Mock
    private RestaurantEntityMapper restaurantEntityMapper;


    @Mock
    private StreetRepository streetRepository;


    @InjectMocks
    private RestaurantRepository restaurantRepository;

    @Test
    void findByName_shouldReturnCorrectRestaurant() {
        // given
        String restaurantName = "Test Restaurant";
        RestaurantEntity restaurantEntity = EntityFixtures.someRestaurantEntity1().withName(restaurantName);
        Restaurant expectedRestaurant = DomainFixtures.someRestaurant1().withName(restaurantName);

        when(restaurantJpaRepository.findByName(restaurantName)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.mapFromEntity(restaurantEntity)).thenReturn(expectedRestaurant);

        // when
        Optional<Restaurant> actualRestaurant = restaurantRepository.findByName(restaurantName);

        // then
        assertThat(actualRestaurant).isPresent();
        assertThat(actualRestaurant.get().getName()).isEqualTo(restaurantName);
    }

    @Test
    void findAll_shouldReturnListOfRestaurants() {

        // given
        String restaurantName1 = "Test Restaurant1";
        RestaurantEntity restaurantEntity1 = EntityFixtures.someRestaurantEntity1().withName(restaurantName1);
        String restaurantName2 = "Test Restaurant2";
        RestaurantEntity restaurantEntity2 = EntityFixtures.someRestaurantEntity2().withName(restaurantName2);
        Restaurant expectedRestaurant1 = DomainFixtures.someRestaurant1().withName(restaurantName1);
        Restaurant expectedRestaurant2 = DomainFixtures.someRestaurant2().withName(restaurantName2);

        when(restaurantJpaRepository.findAll()).thenReturn(List.of(restaurantEntity1, restaurantEntity2));
        when(restaurantEntityMapper.mapFromEntity(restaurantEntity1)).thenReturn(expectedRestaurant1);
        when(restaurantEntityMapper.mapFromEntity(restaurantEntity2)).thenReturn(expectedRestaurant2);

        // when
        List<Restaurant> actualRestaurants = restaurantRepository.findAll();

        // then
        assertThat(actualRestaurants).hasSize(2);
        assertThat(actualRestaurants).contains(expectedRestaurant1, expectedRestaurant2);

    }

//    @Test
//    void findRestaurantsByStreetName_shouldThrowExceptionForInvalidStreetName() {
//        // given
//        String streetName = "Invalid Street";
//        when(streetRepository.findByStreetName(streetName)).thenReturn(Optional.empty());
//
//        // when, then
//        Throwable exception = assertThrows(EntityNotFoundException.class, () -> restaurantRepository.findRestaurantsByStreetName(streetName));
//        Assertions.assertEquals(String.format("Unfortunately, the given street name [%s] is incorrect, please try again", streetName), exception.getMessage());
//    }

    @Test
    void findRestaurantsByOwner_shouldReturnSetOfRestaurants() {
        // Given
        OwnerEntity ownerEntity = EntityFixtures.someOwnerEntity();
        Owner owner = DomainFixtures.someOwner1();

        RestaurantEntity restaurantEntity1 = EntityFixtures.someRestaurantEntity1().withOwner(ownerEntity);
        RestaurantEntity restaurantEntity2 = EntityFixtures.someRestaurantEntity2().withOwner(ownerEntity);

        Set<RestaurantEntity> restaurantEntities = Set.of(restaurantEntity1, restaurantEntity2);

        Restaurant restaurant1 = DomainFixtures.someRestaurant1().withOwner(owner);
        Restaurant restaurant2 = DomainFixtures.someRestaurant2().withOwner(owner);
        Set<Restaurant> expectedRestaurants = Set.of(restaurant1, restaurant2);


        when(restaurantJpaRepository.findRestaurantsByOwner(ownerEntity.getEmail())).thenReturn(restaurantEntities);
        when(restaurantEntityMapper.mapFromEntity(restaurantEntity1)).thenReturn(restaurant1);
        when(restaurantEntityMapper.mapFromEntity(restaurantEntity2)).thenReturn(restaurant2);

        // When
        Set<Restaurant> actualRestaurants = restaurantRepository.findRestaurantsByOwner(ownerEntity.getEmail());

        // Then
        assertThat(actualRestaurants).isEqualTo(expectedRestaurants);
    }


    @Test
    void editRestaurant_shouldReturnMappedEditedRestaurant() {
        // given
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName("Restaurant Name");
        restaurantEntity.setPhone("123456789");
        restaurantEntity.setEmail("restaurant@example.com");
        when(restaurantJpaRepository.findByName("Restaurant Name")).thenReturn(Optional.of(restaurantEntity));

        RestaurantEntity updatedRestaurantEntity = new RestaurantEntity();
        updatedRestaurantEntity.setName("Updated Restaurant Name");
        updatedRestaurantEntity.setPhone("987654321");
        updatedRestaurantEntity.setEmail("updated@example.com");
        when(restaurantJpaRepository.save(restaurantEntity)).thenReturn(updatedRestaurantEntity);

        Restaurant expectedEditedRestaurant = DomainFixtures.someRestaurant1()
                .withName("Updated Restaurant Name")
                .withPhone("987654321")
                .withEmail("updated@example.com");
        when(restaurantEntityMapper.mapFromEntity(updatedRestaurantEntity)).thenReturn(expectedEditedRestaurant);

        // when
        Restaurant editedRestaurant = restaurantRepository.editRestaurant("Restaurant Name", "Updated Restaurant Name", "987654321", "updated@example.com");

        // then
        assertThat(editedRestaurant.getName()).isEqualTo("Updated Restaurant Name");
        assertThat(editedRestaurant.getPhone()).isEqualTo("987654321");
        assertThat(editedRestaurant.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void editRestaurant_shouldThrowExceptionIfRestaurantNotFound() {
        // given
        String restaurantName = "Nonexistent Restaurant";

        when(restaurantJpaRepository.findByName(restaurantName)).thenReturn(Optional.empty());

        // when, then
        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> restaurantRepository
                        .editRestaurant(restaurantName, "someNewName", "somenewPhone", "someNewEmail"));
        Assertions.assertEquals(String.format("Could not find restaurant with name: [%s]", restaurantName), exception.getMessage());
    }
}







