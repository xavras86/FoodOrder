package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.xavras.FoodOrder.api.dto.MenuItemOrderDTO;
import pl.xavras.FoodOrder.domain.MenuItemOrder;

@Mapper(componentModel = "spring")
public interface MenuItemOrderMapper {

    MenuItemOrder map(MenuItemOrderDTO dto);
}
