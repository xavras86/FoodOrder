package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.api.dto.MenuItemOrderDTO;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.MenuItemOrder;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface MenuItemOrderMapper {

    MenuItemOrder map(MenuItemOrderDTO dto);



}
