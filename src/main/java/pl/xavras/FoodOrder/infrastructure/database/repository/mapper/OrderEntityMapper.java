package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {

     @Mapping(target = "menuItemOrders", ignore = true)
     Order mapFromEntity(OrderEntity entity);
//     @Mapping(target = "menuItemOrders", ignore = true)
     OrderEntity mapToEntity(Order order);
}
