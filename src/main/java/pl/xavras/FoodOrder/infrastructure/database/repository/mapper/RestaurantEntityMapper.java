package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.*;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

    Restaurant mapFromEntity (RestaurantEntity entity);

    @Mapping(target = "restaurant.restaurantStreets", ignore = true)
    @Mapping(target = "street.restaurantStreets", ignore = true)
    RestaurantStreet mapFromEntity(RestaurantStreetEntity entity);
//
    RestaurantEntity mapToEntity (Restaurant restaurant);

    @Mapping(target = "restaurant", ignore = true)
    MenuItem mapFromEntity(MenuItemEntity entity);

}
