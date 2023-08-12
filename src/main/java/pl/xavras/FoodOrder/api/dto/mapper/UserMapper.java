package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.xavras.FoodOrder.api.dto.OwnerDTO;
import pl.xavras.FoodOrder.api.dto.UserDTO;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserDTO userDTO);
}
