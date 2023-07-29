package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressEntityMapper {


     Address mapFromEntity(AddressEntity entity);
     AddressEntity mapToEntity(Address address);

}
