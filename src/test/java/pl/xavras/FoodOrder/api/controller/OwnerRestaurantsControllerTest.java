package pl.xavras.FoodOrder.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.api.dto.mapper.MenuItemMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.business.*;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.util.DomainFixtures;
import pl.xavras.FoodOrder.util.DtoFixtures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerRestaurantsControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private OwnerService ownerService;

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private StreetService streetService;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private MenuItemMapper menuItemMapper;

    @Mock
    private UtilityService utilityService;

    @InjectMocks
    private OwnerRestaurantsController ownerRestaurantsController;

    @Test
    void testShowRestaurantsByOwner() {
        // given
        int pageSize = 10;
        int pageNumber = 1;
        String sortBy = "name";
        String sortDirection = "asc";
        Owner activeOwner = DomainFixtures.someOwner1();

        List<Restaurant> restaurantList = List.of(DomainFixtures.someRestaurant1(), DomainFixtures.someRestaurant2());
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurantList);

        Model model = mock(Model.class);

        when(ownerService.activeOwner()).thenReturn(activeOwner);
        when(restaurantService.findByOwner(any(), eq(activeOwner))).thenReturn(restaurantPage);

        // when
        String viewName = ownerRestaurantsController.showRestaurantsByOwner(pageSize, pageNumber, sortBy, sortDirection, model);

        // then
        assertEquals("owner-restaurants", viewName);
        verify(model).addAttribute(eq("totalPages"), eq(restaurantPage.getTotalPages()));
        verify(model).addAttribute(eq("currentPage"), eq(restaurantPage.getNumber() + 1));
        verify(model).addAttribute(eq("pageSize"), eq(pageSize));
        verify(model).addAttribute(eq("sortBy"), eq(sortBy));
        verify(model).addAttribute(eq("sortDirection"), eq(sortDirection));
        verify(model).addAttribute(eq("pageNumbers"), anyList());
    }

    @Test
    void testShowRestaurantMenu() {
        // given
        String restaurantName = "restaurantName";
        int pageSize = 10;
        int pageNumber = 1;
        String sortBy = "name";
        String sortDirection = "asc";

        Restaurant restaurant = DomainFixtures.someRestaurant1();
        when(restaurantService.findByName(restaurantName)).thenReturn(restaurant);

        Page<MenuItem> menuItemsPage = mock(Page.class);
        List<Integer> pageNumbers = List.of(1, 2, 3);

        Model model = mock(Model.class);

        when(menuItemService.getMenuItemsByRestaurantPaged(eq(restaurant), any())).thenReturn(menuItemsPage);
        when(utilityService.generatePageNumbers(anyInt(), anyInt())).thenReturn(pageNumbers);

        // when
        String viewName = ownerRestaurantsController.showRestaurantMenu(
                restaurantName, pageSize, pageNumber, sortBy, sortDirection, model
        );

        // then
        assertEquals("owner-restaurant-details", viewName);
        verify(model).addAttribute("newMenuItem", DtoFixtures.someMenuItemDTO());
        verify(model).addAttribute("menuItems", menuItemsPage.getContent());
        verify(model).addAttribute("totalPages", menuItemsPage.getTotalPages());
        verify(model).addAttribute("currentPage", pageNumber);
        verify(model).addAttribute("pageSize", pageSize);
        verify(model).addAttribute("sortBy", sortBy);
        verify(model).addAttribute("sortDirection", sortDirection);
        verify(model).addAttribute("pageNumbers", pageNumbers);
    }


    @Test
    void testShowMenuItemDetails() {
        // given
        int menuItemId = 1;
        String restaurantName = "restaurantName";

        MenuItem menuItem = DomainFixtures.someMenuItem();
        String imageBase64 = "base64encodedimage";

        Model model = mock(Model.class);

        when(menuItemService.findById(menuItemId)).thenReturn(menuItem);
        when(menuItemService.getString(menuItem)).thenReturn(imageBase64);

        // when
        String viewName = ownerRestaurantsController.showMenuItemDetails(menuItemId, restaurantName, model);

        // then
        assertEquals("owner-menu-item-details", viewName);
        verify(model).addAttribute("menuItem", menuItem);
        verify(model).addAttribute("restaurantName", restaurantName);
        verify(model).addAttribute("imageBase64", imageBase64);
        verify(menuItemService, times(1)).findById(menuItemId);
        verify(menuItemService, times(1)).getString(menuItem);
    }

    @Test
    void testAddMenuItem() throws IOException {
        // given
        MenuItemDTO menuItemDTO = new MenuItemDTO();
        MultipartFile imageFile = new MockMultipartFile("image", new byte[0]);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Restaurant restaurant = DomainFixtures.someRestaurant1();
        String restaurantName = restaurant.getName();
        MenuItem menuItemToSave = DomainFixtures.someMenuItem();


        when(restaurantService.findByName(restaurantName)).thenReturn(restaurant);
        when(menuItemMapper.map(any(MenuItemDTO.class))).thenReturn(menuItemToSave);

        // when
        String viewName = ownerRestaurantsController.addMenuItem(menuItemDTO, restaurantName, imageFile, redirectAttributes);

        // then
        assertEquals("redirect:/owner/restaurants/{restaurantName}", viewName);
        verify(redirectAttributes, times(1)).addAttribute("restaurantName", restaurantName);
        verify(restaurantService, times(1)).findByName(restaurantName);
        verify(menuItemMapper, times(1)).map(menuItemDTO);
    }

    @Test
    void testEditMenuItem() {
        // given
        Integer menuItemId = 1;
        String restaurantName = "restaurantName";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        MenuItem menuItemToEdit = DomainFixtures.someMenuItem();
        when(menuItemService.findById(menuItemId)).thenReturn(menuItemToEdit);

        // when
        String viewName = ownerRestaurantsController.editMenuItem(menuItemId, restaurantName, redirectAttributes);

        // then
        assertEquals("redirect:/owner/restaurants/{restaurantName}", viewName);

        verify(menuItemService, times(1)).findById(menuItemId);
        verify(menuItemService, times(1)).alternateAvailability(menuItemToEdit);
        verify(redirectAttributes, times(1)).addAttribute("restaurantName", restaurantName);

    }
    @Test
    void testAdjustRestaurantRange() {
        // given
        String restaurantName = "restaurantName";
        int pageSize = 10;
        int pageNumber = 1;
        String sortBy = "streetName";
        String sortDirection = "asc";

        Model model = mock(Model.class);
        Page<Street> streetPage = mock(Page.class);
        when(streetPage.getTotalPages()).thenReturn(3);
        List<Street> streetList = new ArrayList<>();

        Map<Street, Boolean> streetStatusMap = new HashMap<>();
        when(streetService.createStreetStatusMap(eq(restaurantName), any(Page.class))).thenReturn(streetStatusMap);

        when(streetService.findAll(any(Pageable.class))).thenReturn(streetPage);

        List<Integer> pageNumbers = List.of(1, 2, 3);
        when(utilityService.generatePageNumbers(anyInt(), anyInt())).thenReturn(pageNumbers);

        // when
        String viewName = ownerRestaurantsController.adjustRestaurantRange(
                restaurantName, model, pageSize, pageNumber, sortBy, sortDirection
        );

        // then
        assertEquals("owner-restaurant-delivery-range", viewName);

        verify(model).addAttribute("restaurantName", restaurantName);
        verify(model).addAttribute("streetStatusMap", streetStatusMap);
        verify(model).addAttribute("totalPages", streetPage.getTotalPages());
        verify(model).addAttribute("currentPage", pageNumber);
        verify(model).addAttribute("pageSize", pageSize);
        verify(model).addAttribute("sortBy", sortBy);
        verify(model).addAttribute("sortDirection", sortDirection);
        verify(model).addAttribute("pageNumbers", pageNumbers);
    }

    @Test
    void testEditDeliveryRange() {
        // given
        String restaurantName = "restaurantName";
        Integer streetId = 1;

        Street street = DomainFixtures.someStreet1();

        when(streetService.findById(streetId)).thenReturn(street);

        // when
        String viewName = ownerRestaurantsController.editDeliveryRange(restaurantName, streetId);

        // then
        assertEquals("redirect:/owner/restaurants/range/{restaurantName}", viewName);

        verify(restaurantService, times(1)).alternateCoverageStateForStreet(restaurantName, street);
    }


}