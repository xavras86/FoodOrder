package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.xavras.FoodOrder.api.dto.mapper.AddressMapper;
import pl.xavras.FoodOrder.api.dto.mapper.OrderMapper;
import pl.xavras.FoodOrder.business.AddressService;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.OrderService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.util.DomainFixtures;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CustomerOrdersController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CustomerOrdersControllerWebMvcTest {

    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private UtilityService utilityService;

    @MockBean
    private AddressMapper addressMapper;
    @MockBean
    private OrderMapper orderMapper;

    @Test
    void testCancelOrder() throws Exception {
        String orderNumber = "123456";
        Order orderToCancel = DomainFixtures.someOrder().withOrderNumber(orderNumber);
        when(orderService.findByOrderNumber(orderNumber)).thenReturn(orderToCancel);

        mockMvc.perform(patch("/customer/orders/cancel/{orderNumber}", orderNumber))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/orders"));

        verify(orderService, times(1)).cancelOrder(orderToCancel);
    }
}



