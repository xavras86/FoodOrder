package pl.xavras.FoodOrder.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.xavras.FoodOrder.domain.Order;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OrdersDTO {

private List<OrderDTO> orders;

}
