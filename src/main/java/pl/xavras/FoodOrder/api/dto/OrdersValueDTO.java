package pl.xavras.FoodOrder.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class OrdersValueDTO {

    private Map<String, BigDecimal> ordersValueMap;

}
