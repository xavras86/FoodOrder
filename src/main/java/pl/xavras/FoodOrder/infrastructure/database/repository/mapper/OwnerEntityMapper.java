package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OwnerEntityMapper {


     Owner mapFromEntity(OwnerEntity entity);

}
