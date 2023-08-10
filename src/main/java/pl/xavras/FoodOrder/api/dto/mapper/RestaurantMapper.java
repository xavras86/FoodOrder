package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.domain.Restaurant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {




    RestaurantDTO map(Restaurant restaurant);

    Restaurant map(RestaurantDTO restaurantDTO);

//    @Named("mapMenuItems")
//    default Set<MenuItemDTO> mapMenuItems(Set<MenuItem> menuItems) {
//        return menuItems.stream().map(this::map).collect(Collectors.toSet());
//    }

//    MenuItemDTO map (MenuItem entity);
}
