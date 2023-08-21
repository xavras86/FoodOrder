package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.xavras.FoodOrder.api.dto.mapper.AddressMapper;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.util.DomainFixtures;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = OwnerOrdersController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerOrdersControllerWebMvcTest {

    @MockBean
    private OrderService orderService;
    @MockBean
    private OwnerService ownerService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private UtilityService utilityService;
    @MockBean
    private OrderMapper orderMapper;
    @MockBean
    private AddressMapper addressMapper;

    private MockMvc mockMvc;

    @Test
    public void testOrdersMethod() throws Exception {

        mockMvc.perform(get(OwnerOrdersController.ORDERS_OWNER))
                .andExpect(status().isOk());

    }
    @Test
    public void testOrderPlaced() throws Exception {
        //given
        String orderNumber = "123";
        Order order = DomainFixtures.someOrder();
        when(orderService.findByOrderNumber(orderNumber)).thenReturn(order);
        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/owner/orders/{orderNumber}", orderNumber))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(orderService, times(1)).findByOrderNumber(orderNumber);
    }

    @Test
    void testCompleteOrder() throws Exception {
        //given
        String orderNumber = "123456";
        Order orderToComplete = DomainFixtures.someOrder().withOrderNumber(orderNumber).withCompleted(false);
        when(orderService.findByOrderNumber(orderNumber)).thenReturn(orderToComplete);
        //when, then
        mockMvc.perform(patch("/owner/orders/complete/{orderNumber}", orderNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owner/orders"));

        verify(orderService, times(1)).completeOrder(orderToComplete);
    }
}



