package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.xavras.FoodOrder.api.dto.OwnerDTO;
import pl.xavras.FoodOrder.domain.Owner;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    OwnerDTO map(Owner owner);
}
