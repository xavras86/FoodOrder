package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.api.dto.MenuItemDTO;
import pl.xavras.FoodOrder.domain.MenuItem;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    MenuItemDTO map(MenuItem menuItem);
    Set<MenuItemDTO> map(Set<MenuItem> menuItem);
}
