package pl.xavras.FoodOrder.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerEntityMapper {

    Customer mapFromEntity(CustomerEntity entity);


    CustomerEntity mapToEntity(Customer customer);

}
