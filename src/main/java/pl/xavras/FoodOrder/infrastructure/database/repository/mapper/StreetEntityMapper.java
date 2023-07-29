package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantStreetEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.StreetEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StreetEntityMapper {
//    @Mapping(target = "restaurantStreets", ignore = true)
//    Street mapFromEntity(StreetEntity entity);


    @Mapping(target = "restaurant.address", ignore = true)
    @Mapping(source = "restaurantStreets", target = "restaurantStreets", qualifiedByName = "mapRestaurantsStreets")
    Street mapFromEntity(StreetEntity entity);


    @Named("mapRestaurantsStreets")
    @SuppressWarnings("unused")
    default Set<RestaurantStreet> mapRestaurantsStreets(Set<RestaurantStreetEntity> entities) {
        return entities.stream().map(this::mapFromEntity).collect(Collectors.toSet());
    }


    @Mapping(target = "street", ignore = true)
    @Mapping(target = "restaurant.address", ignore = true)
    @Mapping(target = "restaurant.restaurantStreets", ignore = true)
    @Mapping(target = "restaurant.menuItems", ignore = true)
    RestaurantStreet mapFromEntity(RestaurantStreetEntity entity);


}



