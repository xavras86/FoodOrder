package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.xavras.FoodOrder.api.dto.StreetDTO;
import pl.xavras.FoodOrder.domain.Street;

@Mapper(componentModel = "spring")
public interface StreetMapper {

    StreetDTO map(Street street);


}
