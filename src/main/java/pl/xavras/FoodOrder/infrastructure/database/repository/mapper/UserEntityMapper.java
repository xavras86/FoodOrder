package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.User;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.security.UserEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {



     default User mapFromEntity(UserEntity entity){
          return User.builder()
                  .email(entity.getEmail())
                  .password(entity.getPassword())
                  .username(entity.getUserName())
                  .role(entity.getRoles().stream().findFirst().get().getRole())
                  .build();
     }
}
