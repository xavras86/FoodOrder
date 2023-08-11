package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantStreetEntityMapper {


//    @Mapping(target = "restaurant", ignore = true) //?
    RestaurantStreet mapFromEntity(RestaurantStreetEntity entity);

    RestaurantStreetEntity mapToEntity(RestaurantStreet restaurantStreet);

    Set<RestaurantStreetEntity> mapToEntity(Set<RestaurantStreet> restaurantStreet);



}
