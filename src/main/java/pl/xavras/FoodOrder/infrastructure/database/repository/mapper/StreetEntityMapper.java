package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.infrastructure.database.entity.StreetEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StreetEntityMapper {


    Street mapFromEntity(StreetEntity entity);
    StreetEntity mapToEntity(Street street);




}



