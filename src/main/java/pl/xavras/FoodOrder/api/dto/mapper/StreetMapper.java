package pl.xavras.FoodOrder.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.xavras.FoodOrder.api.dto.StreetDTO;
import pl.xavras.FoodOrder.domain.Street;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StreetMapper {

    StreetDTO map(Street street);

    List<StreetDTO> map(List<Street> streets);


}
