package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.OrderEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderEntityMapper {


     @Mapping(source = "menuItemOrders", target = "menuItemOrders", qualifiedByName = "mapMenuItemOrders")
     Order mapFromEntity(OrderEntity entity);

     @Mapping(target = "order", ignore = true)
     @Named("mapMenuItemOrders")
     default Set<MenuItemOrder> mapMenuItemOrders(Set<MenuItemOrderEntity> entities) {
          return entities.stream().map(this::mapFromEntity).collect(Collectors.toSet());
     }

     @Mapping(target = "order", ignore = true)
     MenuItemOrder mapFromEntity(MenuItemOrderEntity entity);

     @Mapping(target = "restaurant", ignore = true)
     MenuItem mapFromEntity(MenuItemEntity entity);



//     @Mapping(target = "menuItemOrders", ignore = true)
     OrderEntity mapToEntity(Order order);
}
