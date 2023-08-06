package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemOrderEntityMapper {


     @Mapping(target = "order", ignore = true)
     MenuItemOrder mapFromEntity(MenuItemOrderEntity entity);

     @Mapping(target = "order", ignore = true)
     MenuItemOrderEntity mapToEntity(MenuItemOrder menuItemOrder);

     @Mapping(target = "order", ignore = true)
     Set<MenuItemOrderEntity> mapToEntity(Set <MenuItemOrder> menuItemOrder);


}
