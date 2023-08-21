package pl.xavras.FoodOrder.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.repository.StreetRepository;
import pl.xavras.FoodOrder.util.DomainFixtures;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.xavras.FoodOrder.util.DomainFixtures.someRestaurant1;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {


    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantDAO restaurantDAO;




    @Test
    public void testFindAll() {
        // Given
        List<Restaurant> restaurants = List.of(someRestaurant1(), someRestaurant1());
        when(restaurantDAO.findAll()).thenReturn(restaurants);

        // When
        List<Restaurant> result = restaurantService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindRestaurantsByStreetNamePaged() {
        // Given
        String streetName = "Sample Street";
        Pageable pageable = Pageable.unpaged();
        Page<Restaurant> restaurantPage = Page.empty(pageable);
        when(restaurantDAO.findRestaurantsByStreetNamePaged(streetName, pageable)).thenReturn(restaurantPage);

        // When
        Page<Restaurant> result = restaurantService.findRestaurantsByStreetNamePaged(streetName, pageable);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testFindByNameExistingRestaurant() {
        // Given
        String restaurantName = "Restaurant A";
        Restaurant restaurant = someRestaurant1();
        when(restaurantDAO.findByName(restaurantName)).thenReturn(Optional.of(restaurant));

        // When
        Restaurant result = restaurantService.findByName(restaurantName);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testFindByNameNonExistingRestaurant() {
        // Given
        String restaurantName = "Nonexistent Restaurant";
        when(restaurantDAO.findByName(restaurantName)).thenReturn(Optional.empty());

        // When and Then
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> restaurantService.findByName(restaurantName));
        Assertions.assertEquals(String.format("Restaurant with name [%s] doest not exists", restaurantName), exception.getMessage());
    }


    @Test
    public void testSaveNewRestaurant() {
        // Given
        Restaurant newRestaurant = someRestaurant1();
        when(restaurantDAO.createNewRestaurant(newRestaurant)).thenReturn(newRestaurant);

        // When
        Restaurant result = restaurantService.saveNewRestaurant(newRestaurant);

        // Then
        assertNotNull(result);
    }


    @Test
    public void testFindAllPaged() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<Restaurant> restaurantPage = Page.empty(pageable);
        when(restaurantDAO.findAll(pageable)).thenReturn(restaurantPage);

        // When
        Page<Restaurant> result = restaurantService.findAll(pageable);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testFindByOwner() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Owner owner = DomainFixtures.someOwner1();
        Page<Restaurant> restaurantPage = Page.empty(pageable);
        when(restaurantDAO.findByOwner(pageable, owner)).thenReturn(restaurantPage);

        // When
        Page<Restaurant> result = restaurantService.findByOwner(pageable, owner);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testEditRestaurant() {
        // Given
        String currentName = "Current Restaurant";
        String newName = "New Restaurant";
        String newPhone = "+45 999 888 777";
        String newEmail = "new.restaurant@example.com";
        Restaurant restaurant = someRestaurant1();
        when(restaurantDAO.editRestaurant(currentName, newName, newPhone, newEmail)).thenReturn(restaurant);

        // When
        Restaurant result = restaurantService.editRestaurant(currentName, newName, newPhone, newEmail);

        // Then
        assertNotNull(result);
    }
}