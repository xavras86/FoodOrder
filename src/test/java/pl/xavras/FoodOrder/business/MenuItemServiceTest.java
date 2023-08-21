package pl.xavras.FoodOrder.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.xavras.FoodOrder.business.dao.MenuItemDAO;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.util.DomainFixtures;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {
    @InjectMocks
    private MenuItemService menuItemService;

    @Mock
    private MenuItemDAO menuItemDAO;


    @Test
    public void testSaveMenuItem() {
        // Given
        Restaurant restaurant = DomainFixtures.someRestaurant1();
        MenuItem menuItem = DomainFixtures.someMenuItem();
        when(menuItemDAO.addMenuItem(menuItem, restaurant)).thenReturn(menuItem);

        // When
        MenuItem result = menuItemService.saveMenuItem(menuItem, restaurant);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetMenuItemsByRestaurantPaged() {
        // Given
        Restaurant restaurant = DomainFixtures.someRestaurant1();
        Pageable pageable = Pageable.unpaged();
        List<MenuItem> menuItems = List.of(DomainFixtures.someMenuItem(), DomainFixtures.someMenuItem());
        Page<MenuItem> menuItemPage = Page.empty(pageable);
        when(menuItemDAO.getMenuItemsByRestaurantPaged(restaurant, pageable)).thenReturn(menuItemPage);

        // When
        Page<MenuItem> result = menuItemService.getMenuItemsByRestaurantPaged(restaurant, pageable);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetAvailableMenuItemsByRestaurant() {
        // Given
        Restaurant restaurant = DomainFixtures.someRestaurant1();
        Pageable pageable = Pageable.unpaged();
        List<MenuItem> menuItems = List.of(DomainFixtures.someMenuItem(), DomainFixtures.someMenuItem());
        Page<MenuItem> menuItemPage = Page.empty(pageable);
        when(menuItemDAO.getAvailableMenuItemsByRestaurant(restaurant, pageable)).thenReturn(menuItemPage);

        // When
        Page<MenuItem> result = menuItemService.getAvailableMenuItemsByRestaurant(restaurant, pageable);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAlternateAvailability() {
        // Given
        MenuItem menuItem = DomainFixtures.someMenuItem().withAvailable(true);

        // When
        menuItemService.alternateAvailability(menuItem);

        // Then
        verify(menuItemDAO, times(1)).changeAvailability(menuItem);
    }

    @Test
    public void testFindById() {
        // Given
        Integer menuItemId = 1;
        MenuItem menuItem = DomainFixtures.someMenuItem().withMenuItemId(menuItemId);
        when(menuItemDAO.findById(menuItemId)).thenReturn(menuItem);

        // When
        MenuItem result = menuItemService.findById(menuItemId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getMenuItemId());
    }

    @Test
    public void testFindByName() {
        // Given
        String itemName = "ItemName";
        MenuItem menuItem = DomainFixtures.someMenuItem();
        when(menuItemDAO.findByName(itemName)).thenReturn(menuItem);

        // When
        MenuItem result = menuItemService.findByName(itemName);

        // Then
        assertNotNull(result);
    }

}