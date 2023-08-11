package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.xavras.FoodOrder.api.dto.CustomerAddressOrderDTO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper extends OffsetDateTimeMapper {

//    @Mapping(source = "receivedDateTime", target = "receivedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
//    @Mapping(source = "completedDateTime", target = "completedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
//    OrderDTO map(Order order);

    @Mapping(source = "receivedDateTime", target = "receivedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    @Mapping(source = "completedDateTime", target = "completedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    default CustomerAddressOrderDTO mapToDTO(Order order) {
        return CustomerAddressOrderDTO.builder()
                .name(order.getCustomer().getName())
                .surname(order.getCustomer().getSurname())
                .phone(order.getCustomer().getPhone())
                .email(order.getCustomer().getEmail())
                .country(order.getAddress().getCountry())
                .city(order.getAddress().getCity())
                .street(order.getAddress().getStreet())
                .buildingNumber(order.getAddress().getBuildingNumber())
                .orderNumber(order.getOrderNumber())
                .totalValue(order.getTotalValue())
                .isCancelled(order.getCancelled())
                .isCompleted(order.getCompleted())
                .receivedDateTime(mapOffsetDateTimeToString(order.getReceivedDateTime()))
                .completedDateTime(mapOffsetDateTimeToString(order.getCompletedDateTime()))
                .build();

    }

    @Mapping(source = "receivedDateTime", target = "receivedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    @Mapping(source = "completedDateTime", target = "completedDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    default Order mapFromDTO(CustomerAddressOrderDTO dto) {

        return Order.builder()
                .address(Address.builder()
                        .country(dto.getCountry())
                        .city(dto.getCity())
                        .street(dto.getStreet())
                        .buildingNumber(dto.getBuildingNumber())
                        .build())
                .build();
        //pozostałe pola wyliczane w serwisie
    }

}
