package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

    Restaurant mapFromEntity (RestaurantEntity entity);

    RestaurantEntity mapToEntity (Restaurant restaurant);





}