package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantStreetEntityMapper {


    @Mapping(target = "restaurantStreet.restaurant", ignore = true) //?
    RestaurantStreet mapFromEntity(RestaurantStreetEntity entity);

}
