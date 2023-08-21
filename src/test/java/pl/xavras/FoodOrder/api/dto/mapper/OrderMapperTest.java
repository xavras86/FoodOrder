package pl.xavras.FoodOrder.api.dto.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.api.dto.AddressDTO;
import pl.xavras.FoodOrder.api.dto.CustomerDTO;
import pl.xavras.FoodOrder.api.dto.OrderDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.domain.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @Mock
    private OffsetDateTimeMapper offsetDateTimeMapper;

    @Test
    public void testMapToDTO() {
        // Given
        OffsetDateTime receivedDateTime = OffsetDateTime.of(2023, 8, 21, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime completedDateTime = receivedDateTime.plusHours(2);


        Restaurant restaurant = someRestaurant1()
                .withName("Restaurant ABC")
                .withEmail("restaurant@example.com")
                .withPhone("123-456-789");

        Address address = someAddress1().withCountry("Country")
                .withCity("City")
                .withStreet("Street")
                .withBuildingNumber("123");

        Customer customer = someCustomer()
                .withName("John")
                .withSurname("Doe")
                .withEmail("john@example.com")
                .withPhone("987-654-321");

        MenuItem menuItem = someMenuItem()
                .withName("Burger")
                .withPrice(new BigDecimal("10.00"));

        MenuItemOrder menuItemOrder = someMenuItemOrder()
                .withQuantity(2)
                .withMenuItem(menuItem);

        Order order = someOrder()
                .withOrderNumber("ORD123")
                .withTotalValue(new BigDecimal("50.00"))
                .withCancelled(false).withCompleted(true)
                .withReceivedDateTime(receivedDateTime)
                .withCompletedDateTime(completedDateTime)
                .withRestaurant(restaurant)
                .withAddress(address)
                .withCustomer(customer)
                .withMenuItemOrders(Set.of(menuItemOrder));

        // When
        OrderDTO orderDTO = orderMapper.mapToDTO(order);

        // Then
        assertEquals("ORD123", orderDTO.getOrderNumber());
        assertEquals(new BigDecimal("50.00"), orderDTO.getTotalValue());
        assertEquals(false, orderDTO.getCancelled());
        assertEquals(true, orderDTO.getCompleted());
        assertEquals("2023-08-21 12:00:00", orderDTO.getReceivedDateTime());
        assertEquals("2023-08-21 14:00:00", orderDTO.getCompletedDateTime());

        RestaurantDTO restaurantDTO = orderDTO.getRestaurant();
        assertEquals("Restaurant ABC", restaurantDTO.getName());
        assertEquals("restaurant@example.com", restaurantDTO.getEmail());
        assertEquals("123-456-789", restaurantDTO.getPhone());

        AddressDTO addressDTO = orderDTO.getAddress();
        assertEquals("Country", addressDTO.getCountry());
        assertEquals("City", addressDTO.getCity());
        assertEquals("Street", addressDTO.getStreet());
        assertEquals("123", addressDTO.getBuildingNumber());

        CustomerDTO customerDTO = orderDTO.getCustomer();
        assertEquals("John", customerDTO.getName());
        assertEquals("Doe", customerDTO.getSurname());
        assertEquals("john@example.com", customerDTO.getEmail());
        assertEquals("987-654-321", customerDTO.getPhone());

        assertEquals(1, orderDTO.getMenuItemOrders().size());
        MenuItemOrder menuItemOrderDTO = orderDTO.getMenuItemOrders().stream().findFirst().orElse(null);
        assertEquals(2, Objects.requireNonNull(menuItemOrderDTO).getQuantity());
        assertEquals("Burger", menuItemOrderDTO.getMenuItem().getName());
        assertEquals(new BigDecimal("10.00"), menuItemOrderDTO.getMenuItem().getPrice());
    }
}