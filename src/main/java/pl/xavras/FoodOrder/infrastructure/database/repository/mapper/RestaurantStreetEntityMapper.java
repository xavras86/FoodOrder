package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;

import java.util.Set;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantStreetEntityMapper {



    RestaurantStreet mapFromEntity(RestaurantStreetEntity entity);

    RestaurantStreetEntity mapToEntity(RestaurantStreet restaurantStreet);

    Set<RestaurantStreetEntity> mapToEntity(Set<RestaurantStreet> restaurantStreet);



}