package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {



     Order mapFromEntity(OrderEntity entity);


     OrderEntity mapToEntity(Order order);

     @Mapping(target = "order", ignore = true)
     MenuItemOrder mapMenuItemOrderFromEntity(MenuItemOrderEntity entity);

     @Mapping(target = "order", ignore = true)
     MenuItemOrderEntity mapMenuItemOrderToEntity(MenuItemOrder menuItemOrder);

     @Mapping(target = "restaurant", ignore = true)
     MenuItem mapFromEntity(MenuItemEntity entity);



}
