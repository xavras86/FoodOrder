package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemEntityMapper {


     @Mapping(target = "restaurant", ignore = true)
     MenuItem mapFromEntity(MenuItemEntity entity);


}
