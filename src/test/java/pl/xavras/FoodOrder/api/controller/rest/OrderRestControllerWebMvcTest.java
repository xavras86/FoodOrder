package pl.xavras.FoodOrder.api.controller.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.domain.Order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.xavras.FoodOrder.api.controller.rest.OrderRestController.ORDERS_RESTAURANT_CANCELED_DELETE;
import static pl.xavras.FoodOrder.util.DomainFixtures.someOrder;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = OrderRestController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OrderRestControllerWebMvcTest {

    private final MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderMapper orderMapper;

    @Test
    public void testOrdersList() throws Exception {
        List<Order> orders = List.of(
                someOrder().withOrderNumber("123"),
                someOrder().withOrderNumber("456")
        );
        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders.length()").value(2));
    }


    @Test
    public void testCompletedOrderAndValues() throws Exception {
        //given
        String restaurantName = "TestRestaurant";
        Integer periodDays = 7;
        OffsetDateTime completedDateTime = OffsetDateTime.now().minusDays(5);

        Order order1 = someOrder()
                .withCompleted(true)
                .withCompletedDateTime(completedDateTime.minusDays(5))
                .withOrderNumber("123")
                .withTotalValue(new BigDecimal("100"));
        Order order2 = someOrder()
                .withCompleted(true)
                .withCompletedDateTime(completedDateTime.minusDays(2))
                .withOrderNumber("456")
                .withTotalValue(new BigDecimal("150"));

        Set<Order> orders = Set.of(order1,order2);

        //when
        when(orderService.findByRestaurantName(restaurantName)).thenReturn(orders);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/completed/numbersValuesMap/{restaurantName}/{periodDays}", restaurantName, periodDays))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ordersValueMap").exists());

        verify(orderService, times(1)).findByRestaurantName(restaurantName);
    }




    @Test
    public void testCompletedOrdersListByRestaurant() throws Exception {
        Set<Order> completedOrders = Set.of(
                someOrder().withCompleted(true).withOrderNumber("234"),
                someOrder().withCompleted(true).withOrderNumber("567"));

        when(orderService.findByRestaurantName(anyString())).thenReturn(completedOrders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/completed/{restaurantName}", "SampleRestaurant")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders.length()").value(2));
    }


    @Test
    void testDeleteCanceledOrderOrderNotFound() throws Exception {

        //given
        String orderNumber = "12345";

        when(orderService.findOptByOrderNumber(anyString())).thenReturn(Optional.empty());

        //when, then
        mockMvc.perform(delete(ORDERS_RESTAURANT_CANCELED_DELETE, orderNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));


        verify(orderService, times(1)).findOptByOrderNumber(orderNumber);
        verify(orderService, never()).deleteByOrderNumber(anyString());
    }


}

