package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.xavras.FoodOrder.api.dto.*;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper extends OffsetDateTimeMapper {



    @Mapping(source = "receivedDateTime", target = "receivedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    @Mapping(source = "completedDateTime", target = "completedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    default OrderDTO mapToDTO(Order order) {
        return OrderDTO.builder()
                .restaurant(RestaurantDTO.builder()
                        .name(order.getRestaurant().getName())
                        .email(order.getRestaurant().getEmail())
                        .phone(order.getRestaurant().getPhone())
                        .build())
                .address(AddressDTO.builder()
                        .country(order.getAddress().getCountry())
                        .city(order.getAddress().getCity())
                        .street(order.getAddress().getStreet())
                        .buildingNumber(order.getAddress().getBuildingNumber())
                        .build())
                .customer(CustomerDTO.builder()
                        .name(order.getCustomer().getName())
                        .surname(order.getCustomer().getName())
                        .email(order.getCustomer().getName())
                        .phone(order.getCustomer().getPhone())
                        .build())
                .orderNumber(order.getOrderNumber())
                .totalValue(order.getTotalValue())
                .cancelled(order.getCancelled())
                .completed(order.getCompleted())
                .receivedDateTime(mapOffsetDateTimeToString(order.getReceivedDateTime()))
                .completedDateTime(mapOffsetDateTimeToString(order.getCompletedDateTime()))
                .menuItemOrders(order.getMenuItemOrders())
                .build();

    }

}
