package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.xavras.FoodOrder.api.dto.mapper.AddressMapper;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.util.DomainFixtures;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = OwnerOrdersController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerOrdersControllerWebMvcTest {

    private MockMvc mockMvc;
    @Mock
    private OrderService orderService;
    @Mock
    private OwnerService ownerService;
    @Mock
    private AddressService addressService;
    @Mock
    private UtilityService utilityService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private AddressMapper addressMapper;

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
//                .andExpect(view().name("owner-order-details"))
//                .andExpect(model().attributeExists("order"))
//                .andExpect(model().attributeExists("deliveryAddress"))
//                .andExpect(model().attributeExists("restaurantAddress"))
//                .andExpect(model().attributeExists("menuItemOrders"))
//                .andExpect(model().attributeExists("status"))
//                .andExpect(model().attributeExists("mapUrl"));
//
        Mockito.verify(orderService, Mockito.times(1)).findByOrderNumber(orderNumber);
    }


}