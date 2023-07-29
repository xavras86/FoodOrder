package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.api.dto.CustomerDTO;
import pl.xavras.FoodOrder.domain.Customer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerDTO map(Customer customer);

    Customer map(CustomerDTO customerDTO);
}
